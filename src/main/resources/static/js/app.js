const API_BASE_URL = '/api';
let map;
let routingControl;
let currentMarkerGroup;

// DOM Elements
const authSection = document.getElementById('auth-section');
const appSection = document.getElementById('app-section');
const loginFormContainer = document.getElementById('login-form-container');
const registerFormContainer = document.getElementById('register-form-container');

// State
let token = localStorage.getItem('token');
let user = null;

document.addEventListener('DOMContentLoaded', () => {
    initAuthForms();
    checkAuth();
});

// --- Auth logic ---
function initAuthForms() {
    document.getElementById('show-register').addEventListener('click', (e) => {
        e.preventDefault();
        loginFormContainer.classList.add('hidden');
        registerFormContainer.classList.remove('hidden');
    });

    document.getElementById('show-login').addEventListener('click', (e) => {
        e.preventDefault();
        registerFormContainer.classList.add('hidden');
        loginFormContainer.classList.remove('hidden');
    });

    document.getElementById('login-form').addEventListener('submit', handleLogin);
    document.getElementById('register-form').addEventListener('submit', handleRegister);
    document.getElementById('btn-logout').addEventListener('click', handleLogout);
}

async function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    const btn = document.getElementById('btn-login');
    const errDiv = document.getElementById('login-error');

    try {
        btn.textContent = 'Đang đăng nhập...';
        btn.disabled = true;
        const res = await axios.post(`${API_BASE_URL}/auth/login`, { email, password });
        // The API might return token in different formats. Assume standard res.data.token
        // Note: Assuming JWT token is returned in res.data.token based on standard spring-boot jjwt implementation
        if (res.data.token) {
            token = res.data.token;
            localStorage.setItem('token', token);
            await fetchUserAndInitApp();
        } else {
            errDiv.textContent = 'Phản hồi từ máy chủ không hợp lệ!';
            errDiv.classList.remove('hidden');
        }
    } catch (err) {
        errDiv.textContent = err.response?.data?.message || 'Đăng nhập thất bại. Sai email hoặc mật khẩu!';
        errDiv.classList.remove('hidden');
    } finally {
        btn.textContent = 'Đăng nhập';
        btn.disabled = false;
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const fullName = document.getElementById('reg-name').value;
    const email = document.getElementById('reg-email').value;
    const phone = document.getElementById('reg-phone').value;
    const password = document.getElementById('reg-password').value;
    const role = document.getElementById('reg-role').value;
    
    const btn = document.getElementById('btn-register');
    const errDiv = document.getElementById('register-error');

    try {
        btn.textContent = 'Đang đăng ký...';
        btn.disabled = true;
        await axios.post(`${API_BASE_URL}/auth/register`, { fullName, email, phone, password, role });
        // After register, auto jump to login or auto login (here we just hide register and show login)
        alert('Đăng ký thành công! Vui lòng đăng nhập.');
        document.getElementById('show-login').click();
    } catch (err) {
        errDiv.textContent = err.response?.data?.message || 'Đăng ký thất bại. Email hoặc SĐT có thể đã tồn tại.';
        errDiv.classList.remove('hidden');
    } finally {
        btn.textContent = 'Đăng ký';
        btn.disabled = false;
    }
}

function handleLogout() {
    localStorage.removeItem('token');
    token = null;
    user = null;
    appSection.classList.add('hidden');
    authSection.classList.remove('hidden');
    // Clear map/intervals
}

async function checkAuth() {
    if (token) {
        await fetchUserAndInitApp();
    }
}

async function fetchUserAndInitApp() {
    try {
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        const res = await axios.get(`${API_BASE_URL}/auth/me`);
        user = res.data;
        
        // Show App UI
        authSection.classList.add('hidden');
        appSection.classList.remove('hidden');
        document.getElementById('user-greeting').textContent = `Xin chào, ${user.fullName}`;
        
        initDashboard();
        initMap();
        fetchMyBookings();
    } catch (error) {
        console.error('Lỗi lấy thông tin user, xoá token', error);
        localStorage.removeItem('token');
        token = null;
        authSection.classList.remove('hidden');
    }
}

// --- App Logic ---
function initDashboard() {
    const isPassenger = user.role.toUpperCase() === 'PASSENGER' || user.role.toUpperCase() === 'ROLE_PASSENGER';
    if (isPassenger) {
        document.getElementById('passenger-dashboard').classList.remove('hidden');
        document.getElementById('driver-dashboard').classList.add('hidden');
        document.getElementById('booking-form').addEventListener('submit', handleBookRide);
        setupLocationMocking();
    } else {
        document.getElementById('passenger-dashboard').classList.add('hidden');
        document.getElementById('driver-dashboard').classList.remove('hidden');
        fetchAvailableBookings();
        setInterval(fetchAvailableBookings, 10000); // Poll every 10s for new bookings
    }
}

// Map initialization with Leaflet
function initMap() {
    if (map) return; // Already init
    
    // Default config: Hanoi coordinates
    map = L.map('map', {
        zoomControl: false 
    }).setView([21.028511, 105.804817], 13);
    
    L.control.zoom({ position: 'bottomright' }).addTo(map);

    L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
        subdomains: 'abcd',
        maxZoom: 20
    }).addTo(map);

    currentMarkerGroup = L.layerGroup().addTo(map);
    
    setTimeout(() => {
        map.invalidateSize();
        document.getElementById('map-loading').classList.add('hidden');
    }, 1000);
}

// Mocking some location search behavior
function setupLocationMocking() {
    const pickup = document.getElementById('pickup-location');
    const dropoff = document.getElementById('dropoff-location');
    
    // Very basic distance mock when both are filled
    const calcBtn = () => {
        if(pickup.value && dropoff.value) {
            const mockDistance = (Math.random() * 15 + 2).toFixed(1);
            document.getElementById('trip-distance').textContent = mockDistance;
            document.getElementById('trip-price').textContent = (mockDistance * 12000).toLocaleString();
            
            // Draw dummy markers on map
            currentMarkerGroup.clearLayers();
            const lat1 = 21.028511 + (Math.random() - 0.5) * 0.05;
            const lng1 = 105.804817 + (Math.random() - 0.5) * 0.05;
            const lat2 = lat1 + (Math.random() - 0.5) * 0.05;
            const lng2 = lng1 + (Math.random() - 0.5) * 0.05;
            
            L.marker([lat1, lng1]).addTo(currentMarkerGroup).bindPopup('Điểm đón').openPopup();
            L.marker([lat2, lng2]).addTo(currentMarkerGroup).bindPopup('Điểm đến');
            
            const group = new L.featureGroup([L.marker([lat1, lng1]), L.marker([lat2, lng2])]);
            map.fitBounds(group.getBounds().pad(0.5));
            
            // Draw a polyline connection
            L.polyline([[lat1, lng1], [lat2, lng2]], {color: '#ec4899', weight: 4, dashArray: '10, 10'}).addTo(currentMarkerGroup);
        }
    };

    pickup.addEventListener('blur', calcBtn);
    dropoff.addEventListener('blur', calcBtn);
}


async function handleBookRide(e) {
    e.preventDefault();
    const pickupLocation = document.getElementById('pickup-location').value;
    const dropoffLocation = document.getElementById('dropoff-location').value;
    const distanceKm = parseFloat(document.getElementById('trip-distance').textContent) || Math.random() * 10;
    
    // Create Booking Request structure based on CreateBookingRequest DTO (guessing standard fields)
    const requestData = {
        pickupLocation,
        dropoffLocation,
        distanceKm
    };

    const btn = document.getElementById('btn-book');
    try {
        btn.textContent = 'Đang gửi...';
        btn.disabled = true;
        const res = await axios.post(`${API_BASE_URL}/bookings`, requestData);
        alert('Đặt chuyến thành công! Vui lòng chờ tài xế.');
        document.getElementById('booking-form').reset();
        document.getElementById('trip-distance').textContent = '--';
        document.getElementById('trip-price').textContent = '--';
        fetchMyBookings();
    } catch (err) {
        alert('Lỗi đặt xe: ' + (err.response?.data?.message || err.message));
    } finally {
        btn.textContent = 'Yêu cầu Đặt Xe';
        btn.disabled = false;
    }
}

async function fetchMyBookings() {
    try {
        const res = await axios.get(`${API_BASE_URL}/bookings`);
        const listContainer = document.getElementById('my-bookings-list');
        listContainer.innerHTML = '';
        
        if (res.data && res.data.length > 0) {
            res.data.reverse().forEach(b => { // Show newest first
                const li = document.createElement('li');
                li.innerHTML = `
                    <span class="status-badge status-${b.status}">${b.status}</span>
                    <div><strong>Từ:</strong> ${b.pickupLocation}</div>
                    <div><strong>Đến:</strong> ${b.dropoffLocation}</div>
                    <div class="text-secondary" style="font-size: 0.8rem">Khoảng cách: ${b.distanceKm} km</div>
                `;
                listContainer.appendChild(li);
            });
        } else {
            listContainer.innerHTML = '<li class="text-muted text-center" style="border:none">Chưa có chuyến đi nào.</li>';
        }
    } catch (err) {
        console.error('Error fetching bookings', err);
    }
}

async function fetchAvailableBookings() {
    // For driver, fetching ALl pending bookings. In a real app we might need an endpoint like /bookings/pending
    // Here we just fetch all and filter by PENDING
    try {
        const res = await axios.get(`${API_BASE_URL}/bookings`);
        const listContainer = document.getElementById('available-bookings');
        listContainer.innerHTML = '';
        
        const pendingBookings = res.data.filter(b => b.status === 'PENDING');
        
        if (pendingBookings.length > 0) {
            pendingBookings.forEach(b => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <span class="status-badge status-PENDING">MỚI CHỜ NHẬN</span>
                    <div><strong>Từ:</strong> ${b.pickupLocation}</div>
                    <div><strong>Đến:</strong> ${b.dropoffLocation}</div>
                    <div style="margin-top: 8px">
                        <button class="btn btn-primary btn-sm" onclick="acceptBooking(${b.id})">Nhận Cuốc (Mô phỏng)</button>
                    </div>
                `;
                listContainer.appendChild(li);
            });
        } else {
            listContainer.innerHTML = '<li class="text-muted text-center" style="border:none">Hiện không có cuốc xe nào đang chờ.</li>';
        }
    } catch (err) {
        console.error('Error fetching available bookings', err);
    }
}

// Global scope window access for button click
window.acceptBooking = async function(id) {
    try {
        await axios.put(`${API_BASE_URL}/bookings/${id}`, { status: 'ACCEPTED' });
        alert('Nhận chuyến thành công!');
        fetchAvailableBookings();
        fetchMyBookings();
    } catch (err) {
        alert('Lỗi nhận xe: ' + (err.response?.data?.message || err.message));
    }
};
