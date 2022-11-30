create table `URL_MAPPINGS`(
	`id` bigint not null auto_increment,
    `short_url` varchar(6) not null,
    `url` blob not null,
    `checksum` int not null,
    `insert_ts` datetime not null default now(),
    `version` int not null default 1,
    primary key (`id`)
);
create index `idx_url_mappings_short_url` on URL_MAPPINGS(short_url);
create index `idx_url_mappings_checksum` on URL_MAPPINGS(checksum);
