create table activities(id bigint auto_increment,
                        start_time timestamp not null,
                        act_desc varchar(255) not null,
--                        act_type ENUM('BIKING', 'HIKING', 'RUNNING', 'BASKETBALL') not null,
                        act_type varchar(20) not null,
                        constraint pk_activities primary key (id));