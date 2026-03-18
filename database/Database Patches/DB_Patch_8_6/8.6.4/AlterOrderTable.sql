alter table xhb_order add email_id number;
alter table xhb_order add foreign key (email_id) references xhb_email2(mail_id);

commit;