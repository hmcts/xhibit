set termout off;
delete from aud_user_logins where rownum<2;
#update aud_user_logins set logged_in='N' where lower(trim(user_id))=lower(trim('&1'));
exit;
