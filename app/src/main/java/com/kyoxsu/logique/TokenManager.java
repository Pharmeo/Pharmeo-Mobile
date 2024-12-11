package com.kyoxsu.logique;
//------------------------------------------------------------------------------
/**
 *
 */
//------------------------------------------------------------------------------
public class TokenManager
{
    private static TokenManager instance;
    private final String token;

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // Constructeur privé
    private TokenManager(String token)
    {
        this.token = token;
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // Méthode pour obtenir l'instance unique
    public static TokenManager getInstance(String token)
    {
        if (instance == null)
        {
            instance = new TokenManager(token);
        }
        return instance;
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // Méthode pour obtenir le token
    public String getToken()
    {
        return token;
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // Méthode pour réinitialiser l'instance
    /*
    public static void resetInstance(String newToken)
    {
        // TODO : A modifier car je souhaite que cette classe ne soit pas modifiable
        instance = new TokenManager(newToken);
    }
    */
}

