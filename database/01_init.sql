CREATE TABLE tb_board
(
    id         VARCHAR(72)  NOT NULL,
    title      VARCHAR(200)  NOT NULL,
    body       VARCHAR(1500) NULL,
    created_at datetime      NOT NULL default now(),
    writer     VARCHAR(100)  NOT NULL,
    CONSTRAINT pk_tb_board PRIMARY KEY (id)
);

CREATE INDEX idx_created_at ON tb_board (created_at);

CREATE INDEX idx_writer ON tb_board (writer);


##
