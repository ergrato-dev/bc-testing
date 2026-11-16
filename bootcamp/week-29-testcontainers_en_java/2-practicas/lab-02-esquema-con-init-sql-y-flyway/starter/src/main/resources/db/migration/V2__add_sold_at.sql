ALTER TABLE tickets ADD COLUMN sold_at timestamptz NOT NULL DEFAULT now();
