 set -x
 curl -i -X POST \
 http://localhost:8080/ \
-X POST \
-H "Ce-Id: 1234" \
-H "Ce-Specversion: 1.0" \
-H "Ce-Type: lala" \
-H "Ce-Source: curl" \
-H "Ce-rhaccount: 12345" \
-H "Content-Type: application/json" \
-d @- << -EOF-
{
  "status": "Success",
  "message": "li la lu",
  "template": "11",
  "job": "20"

}
-EOF-
