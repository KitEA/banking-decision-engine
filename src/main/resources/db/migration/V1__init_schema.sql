CREATE TABLE segments (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    credit_modifier INTEGER NOT NULL
);

CREATE TABLE credit_profiles (
    personal_code VARCHAR(11) PRIMARY KEY,
    segment_id BIGINT NOT NULL,
    CONSTRAINT fk_credit_profile_segment
        FOREIGN KEY (segment_id)
        REFERENCES segments (id)
);