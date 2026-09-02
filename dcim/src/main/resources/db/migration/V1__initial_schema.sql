CREATE TABLE companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    address VARCHAR(255),
    description TEXT
);

CREATE TABLE organizational_units (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    CONSTRAINT fk_ou_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT uq_ou_company_name UNIQUE (company_id, name)
);

CREATE TABLE service_units (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizational_unit_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    CONSTRAINT fk_su_ou FOREIGN KEY (organizational_unit_id) REFERENCES organizational_units(id),
    CONSTRAINT uq_su_ou_name UNIQUE (organizational_unit_id, name)
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_unit_id BIGINT NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_user_service_unit FOREIGN KEY (service_unit_id) REFERENCES service_units(id),
    CONSTRAINT chk_user_role CHECK (role IN ('ADMIN', 'USER'))
);

CREATE TABLE devices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_unit_id BIGINT NOT NULL,
    responsible_user_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    hostname VARCHAR(255) UNIQUE,
    ip_address VARCHAR(45),
    serial_number VARCHAR(100) UNIQUE,
    manufacturer VARCHAR(100),
    model VARCHAR(100),
    device_type VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_device_service_unit FOREIGN KEY (service_unit_id) REFERENCES service_units(id),
    CONSTRAINT fk_device_responsible FOREIGN KEY (responsible_user_id) REFERENCES users(id),
    CONSTRAINT chk_device_type CHECK (device_type IN ('PHYSICAL_SERVER','VIRTUAL_SERVER','NETWORK_DEVICE','STORAGE','OTHER')),
    CONSTRAINT chk_device_status CHECK (status IN ('ACTIVE','INACTIVE','MAINTENANCE','RETIRED'))
);

CREATE TABLE software (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    vendor VARCHAR(150) NOT NULL,
    version VARCHAR(100) NOT NULL,
    software_type VARCHAR(30) NOT NULL,
    description TEXT,
    CONSTRAINT uq_software_identity UNIQUE (name, vendor, version),
    CONSTRAINT chk_software_type CHECK (software_type IN ('OPERATING_SYSTEM','DATABASE','APPLICATION','SECURITY','MONITORING','OTHER'))
);

CREATE TABLE software_licenses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    software_id BIGINT NOT NULL,
    license_key VARCHAR(255),
    license_type VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    quantity INT NOT NULL DEFAULT 1,
    purchase_price DECIMAL(12,2),
    notes TEXT,
    CONSTRAINT fk_license_software FOREIGN KEY (software_id) REFERENCES software(id),
    CONSTRAINT chk_license_type CHECK (license_type IN ('SUBSCRIPTION','PERPETUAL','TRIAL')),
    CONSTRAINT chk_license_dates CHECK (end_date IS NULL OR end_date >= start_date),
    CONSTRAINT chk_license_quantity CHECK (quantity > 0)
);

CREATE INDEX idx_license_end_date ON software_licenses(end_date);

CREATE TABLE software_installations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id BIGINT NOT NULL,
    software_id BIGINT NOT NULL,
    license_id BIGINT,
    installation_date DATE,
    installed_version VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'INSTALLED',
    CONSTRAINT fk_installation_device FOREIGN KEY (device_id) REFERENCES devices(id),
    CONSTRAINT fk_installation_software FOREIGN KEY (software_id) REFERENCES software(id),
    CONSTRAINT fk_installation_license FOREIGN KEY (license_id) REFERENCES software_licenses(id),
    CONSTRAINT uq_installation_device_software UNIQUE (device_id, software_id),
    CONSTRAINT chk_installation_status CHECK (status IN ('INSTALLED','REMOVED'))
);

CREATE TABLE license_renewals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    license_id BIGINT NOT NULL,
    renewed_by_user_id BIGINT NOT NULL,
    previous_end_date DATE,
    new_end_date DATE NOT NULL,
    renewed_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    note TEXT,
    CONSTRAINT fk_renewal_license FOREIGN KEY (license_id) REFERENCES software_licenses(id),
    CONSTRAINT fk_renewal_user FOREIGN KEY (renewed_by_user_id) REFERENCES users(id),
    CONSTRAINT chk_renewal_dates CHECK (previous_end_date IS NULL OR new_end_date > previous_end_date)
);
