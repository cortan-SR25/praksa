CREATE INDEX idx_renewal_license_date ON license_renewals(license_id, renewed_at);
CREATE INDEX idx_installation_license_status ON software_installations(license_id, status);
