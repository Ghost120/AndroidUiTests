<h1>Push Test Project</h1>
<b>Cloud function to send push:</b>
https://us-central1-pushtestproject-7f507.cloudfunctions.net/sendPush<br/>
<h2>Project description</h2>
Application screen shows EUR/USD quote with time of last update.
<br/>
When push notification will be received by the app, quotes request will be executed and result will be saved to the database in background even if the application is not running.
<br/>
Pushes sent by cron every 10 minutes.<br/>
<br/>
