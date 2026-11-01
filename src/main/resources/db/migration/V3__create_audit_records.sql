CREATE TABLE IF NOT EXISTS audit_records (
                                             id BIGSERIAL PRIMARY KEY,
                                             message VARCHAR(255) NOT NULL
    );
