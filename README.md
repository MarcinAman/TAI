# TAI
### Sample graphQL queries:
Fetching all currencies:
```json
{
	"query": "query { currencies { currencyName code } }"
}
```

Fetching latest rates: 
```json
{
	"query": "query { exchanges { ID date rates { mid currency { currencyName code }} } }"
}
```
Fetching data from period:
```json
{
	"query": "query sample($code: String!,$from: DateTime!, $to: DateTime!){ currencyFromPeriod(code: $code, from: $from, to: $to) { currencyPeriod { from to currency { currencyName code } } rates { date bid } } }",
	"variables": {
		"code" : "USD",
		"from" : "2019-03-15T00:00:00", 
		"to": "2019-04-15T00:00:00"
	}
}
```

### Building docker images:
Backend:
```bash
./buildDockerBackend.sh 
```

Frontend:
```bash
cd ui
./buildDockerFrontend.sh
```

Running:
```bash
docker-compose -f ./conf/docker-compose up
```

Application is hosted by default on 80 port

