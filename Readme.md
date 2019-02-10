<h1>Alexa Hue Sunrise Alarm</h1>

Requires Java JDK 11+

To build:
<pre>
./gradlew dockerBuildImage 
</pre>

To run:
<pre>
docker run -e TZ=America/New_York \
           -e HUE_ROOM_NAME=Bedroom \
           -e HUE_API_KEY=Your api key \
           -e HUE_BRIDGE_IP_ADDRESS=192.168.1.151 \
           -e ALEXA_DEVICE_NAME="Bedroom Echo Dot" \
           -e ALEXA_AUTHENTICATION_COOKIE='TODO: write how to get this cookie' \
           -e REFRESH_INTERVAL=60 \
           -e PRE_ALARM_SEQUENCE_DURATION=30 \
           alexa-hue-sunrise-alarm:latest
</pre>

TODO: explain environment variables, note usage of alexa.amazon.com/api

Thanks to <a href="https://github.com/ZeroOne3010">ZeroOne3010</a> for the <a href="https://github.com/ZeroOne3010/yetanotherhueapi">yetanotherhueapi</a> hue bridge client library and to <a href="https://community.home-assistant.io/t/wake-up-sunrise-simulation-with-the-philips-hue-and-sleep-as-android/82437">farlock85</a> for a list of <a href="https://community.home-assistant.io/t/wake-up-sunrise-simulation-with-the-philips-hue-and-sleep-as-android/82437">sunrise colors</a>.