# screener-user

1. ngrok http 8080
2. Copy "Forwarding" section
3. curl -s -X POST "https://api.telegram.org/bot8410759213:AAHBi8RHG3tDwlNylQI1V-6krT43YdIOa3Y/setWebhook" -d "url=https://f2452cbeb3e2.ngrok-free.app/telegram/webhook" -d "secret_token=secret" -d "drop_pending_updates=true" -d "allowed_updates[]=message" -d "allowed_updates[]=callback_query"
4. check state on curl -s "https://api.telegram.org/bot8410759213:AAHBi8RHG3tDwlNylQI1V-6krT43YdIOa3Y/getWebhookInfo"