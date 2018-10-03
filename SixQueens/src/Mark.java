public enum Mark
{
    /**
     * Square is blank.
     */
    BLANK,

    /**
     * Square is marked X.
     */
    X,

    /**
     * Square is marked O.
     */
    O;

    /**
     * Convert the given ordinal to an enumeral.
     *
     * @param  ordinal  Ordinal.
     *
     * @return  Enumeral.
     *
     * @exception  IllegalArgumentException
     *     (unchecked exception) Thrown if <TT>ordinal</TT> is invalid.
     */
    public static Mark valueOf
    (int ordinal)
    {
        switch (ordinal)
        {
            case 0: return BLANK;
            case 1: return X;
            case 2: return O;
            default:
                throw new IllegalArgumentException (String.format
                        ("Mark.valueOf(): ordinal = %d illegal", ordinal));
        }
    }

    // Note: Use the <TT>ordinal()</TT> method to convert an enumeral to an
    // ordinal.

}
