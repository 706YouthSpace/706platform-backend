insert into index_activities(`id`, `name`, `image`, `datetime`) values
    (1, '测试活动1', 'https://img.ivsky.com/img/bizhi/pre/201105/16/macintosh-028.jpg', '2020-07-01 14:30:00'),
    (2, '测试活动2', 'https://img.ivsky.com/img/bizhi/pre/201105/16/macintosh-030.jpg', '2020-07-02 14:30:00'),
    (3, '测试活动3', 'https://img.ivsky.com/img/bizhi/pre/201105/16/macintosh-032.jpg', '2020-07-03 14:30:00'),
    (4, '测试活动4', 'https://img.ivsky.com/img/bizhi/pre/201105/16/macintosh-036.jpg', '2020-07-04 14:30:00'),
    (5, '测试活动5', 'https://img.ivsky.com/img/bizhi/pre/201105/16/macintosh-028.jpg', '2020-07-05 14:30:00');

insert into accounts( account, password, uid) values ( 'guest@example.com', 'guest', 1);
insert into authorities(uid, authority) VALUES (1, 'USER');
insert into profiles(id, last_name, first_name) values (1, 'Last Name', 'First Name');

