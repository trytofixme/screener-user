# screener-user

1. ngrok http 8080
2. Copy "Forwarding" section
3. curl -s -X POST "https://api.telegram.org/bot8276706177:AAHFWLZPfGW8oYMysqDgVheIKasfENdjr-c/setWebhook" -d "url=https://candescent-janie-unadoring.ngrok-free.dev/telegram/webhook" -d "secret_token=58c5dd89-226e-4009-908e-b1873b1d4808" -d "drop_pending_updates=true" -d "allowed_updates[]=message" -d "allowed_updates[]=callback_query"
4. check state on curl -s "https://api.telegram.org/bot8276706177:AAHFWLZPfGW8oYMysqDgVheIKasfENdjr-c/getWebhookInfo"

# TODO:
1. Прикрутить R2DBC
2. Реализовать механизм подписок