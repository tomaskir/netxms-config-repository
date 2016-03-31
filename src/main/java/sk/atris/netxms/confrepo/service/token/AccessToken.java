package sk.atris.netxms.confrepo.service.token;

abstract class AccessToken {
    private String token;

    private boolean tokenRead = false;

    public String getToken() {
        if (!tokenRead) {
            token = loadToken();

            tokenRead = true;
        }

        return token;
    }

    abstract String loadToken();
}
