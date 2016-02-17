# OpenHRM
Open Source Human Resource Management Software

Required entries in database:

insert into additional_roles(description) values ("ROLE_HR");
insert into additional_roles(description) values ("ROLE_MANAGER");

insert into employee values(1, now(), 'admin@openhrm.io', 0, 0, 'Administrator', sha1('admin@openhrm1234$'), 0, null);
insert into employee_additional_roles values(1, 1);