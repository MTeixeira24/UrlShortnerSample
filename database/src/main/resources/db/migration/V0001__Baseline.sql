create table `url_mappings`(
	`id` bigint not null auto_increment,
    `short_url` varchar(8) not null,
    `url` blob not null,
    `checksum` bigint not null unique,
    `insert_ts` datetime default now(),
    `version` int default 1,
    primary key (`id`)
);
create index `idx_url_mappings_short_url` on url_mappings(short_url);
create index `idx_url_mappings_checksum` on url_mappings(checksum);

create table `redirect_service_domain` (
    `id` int not null auto_increment,
    `domain` varchar(255) not null,
    primary key (`id`)
);