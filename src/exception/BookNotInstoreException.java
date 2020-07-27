package exception;

public class BookNotInstoreException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	public BookNotInstoreException(){
		System.out.println("害 来晚了 书已经被有缘人借走了啦 下次再来吧");
	}
}
