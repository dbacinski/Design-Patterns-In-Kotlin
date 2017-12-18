class main
{
    companion object
    {
        @JvmStatic
        fun main(args: Array<String>)
        {
            println(Singleton.getSingleton().toString())
        }
    }

}

/*
    class Singleton, private constructor so I won't be able to construct outside
 */
class Singleton private constructor()
{
    override fun toString():String = "Singleton Instance"

    /*
        to initate singleton, I need some form of "static", companion object is my alternative
     */
    companion object
    {
        /*
            instance object representation, inits to null
         */
        private var Instance:Singleton? = null

        /*
            getInstance, function is already there though
         */
        public fun getSingleton():Singleton
        {
            /*
                case we got an instance already
             */
            return if(Instance != null)
                Instance!!
            else
            {
                /*
                case we need a new instance
                 */
                Instance = Singleton()
                Instance!!
            }
        }
    }
}
