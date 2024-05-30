--Please run below query to create a system user before application start

INSERT into users(id,user_id,first_name,last_name,email,phone,bio,reference_id,image_url,created_by,updated_by,created_at,
                  updated_at,account_non_expired,account_non_locked,enabled)
values
    (0,'368537c3-fa14-4133-9143-0739ba2b23cf','System','System','system@gmail.com','9898987766','This is not any user but system itslef',
     '368537c3-fa14-4133-9143-0739cd8n23cf','https://freepngimg.com/thumb/emoji/73723-emoticon-smiley-sticker-honda-up-amaze-thumbs.png',
     0,0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,TRUE,TRUE,TRUE)

-- CREATE INDEX IF NOT EXISTS index_users_email ON users(email);
-- CREATE INDEX IF NOT EXISTS index_users_user_id ON users(user_id);
-- CREATE INDEX IF NOT EXISTS index_confirmations_user_id ON confirmations(user_id);
-- CREATE INDEX IF NOT EXISTS index_credentials_user_id ON credentials(user_id);
-- CREATE INDEX IF NOT EXISTS index_user_roles_user_id ON user_roles(user_id);
