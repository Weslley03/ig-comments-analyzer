CREATE TABLE plans (
    id          BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    price       NUMERIC(10,2) NOT NULL,
    role        VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE role_code (
    role               VARCHAR(100) PRIMARY KEY REFERENCES plans(role),
    usage_limit        INTEGER NOT NULL,
    comments_per_query INTEGER NOT NULL
);

CREATE TABLE users (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role_code  VARCHAR(100) NOT NULL REFERENCES role_code(role),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE permissions (
    id                 BIGSERIAL PRIMARY KEY,
    user_id            UUID NOT NULL UNIQUE REFERENCES users(id),
    role_code          VARCHAR(100) NOT NULL REFERENCES role_code(role),
    usage_limit        INTEGER NOT NULL,
    remaining_limit    INTEGER NOT NULL,
    comments_per_query INTEGER NOT NULL
);

CREATE TABLE user_plans (
    id         BIGSERIAL PRIMARY KEY,
    user_id    UUID NOT NULL REFERENCES users(id),
    plan_id    BIGINT NOT NULL REFERENCES plans(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE comment_analysis (
    id                  BIGSERIAL PRIMARY KEY,
    ig_url              TEXT NOT NULL,
    description_content TEXT,
    result              TEXT NOT NULL,
    metadata            JSONB NOT NULL DEFAULT '{}',
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE comment_analysis_history (
    id          BIGSERIAL PRIMARY KEY,
    analysis_id BIGINT NOT NULL REFERENCES comment_analysis(id),
    user_id     UUID NOT NULL REFERENCES users(id),
    label       VARCHAR(150) NOT NULL,
    verdict     VARCHAR(10) NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
