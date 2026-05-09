INSERT INTO plans (description, price, role)
VALUES ('Plano Iniciante — acesso gratuito com uso limitado', 0.00, 'PLAN_BEGINNER');

INSERT INTO role_code (role, usage_limit, comments_per_query)
VALUES ('PLAN_BEGINNER', 1, 20);
