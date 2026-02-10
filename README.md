## Testar Spring Boot application events med och utan @Async och transaktioner.

### Endpoints

Se [HTTP-requests.http](HTTP-requests.http).

### Lyssnare

#### MyEventSyncListener
Kommer få alla events, oavsett om transaktionen lyckas eller ej.

#### MyEventAsyncListener
Kommer endast få event för endpointen `commit`. Detta pga att `rollback` rullar tillbaka transaktionen och `none` inte ens har någon transaktion. 