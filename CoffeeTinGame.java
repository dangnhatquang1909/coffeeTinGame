package a1_2001040173;

import java.util.Arrays;
import utils.TextIO;

/**
 * @overview A program that performs the coffee tin game on a tin of beans and
 *           display result on the standard output.
 * 
 */
public class CoffeeTinGame {
	/** constant value for the green bean */
	private static final char GREEN = 'G';
	/** constant value for the blue bean */
	private static final char BLUE = 'B';
	/** constant for removed beans */
	private static final char REMOVED = '-';
	/** the null character */
	private static final char NULL = '\u0000';

	/**
	 * @requires BeansBag can hold up to 30 beans
	 * @modifies BeansBag
	 * @effects choose a beans randomly if belongs to 10 lower that blue, between 11
	 *          and 20 are green, others are available spaces
	 */
	private static final char[] BeansBag = new char[30];
	static {
		for (int pos1 = 1; pos1 < 30; pos1++) {
			if (pos1 < 11) {
				BeansBag[pos1] = BLUE;// return blue bean
			} else if (pos1 < 21) {
				BeansBag[pos1] = GREEN;// return green bean
			}
		}
	}

	/**
	 * the main procedure
	 * 
	 * @effects <tt>
	 * 		initialize a coffee tin 
	 * 		{@link TextIO#putf(String, Object...)}: print the tin content 
	 *      {@link @tinGame(char[])}: perform the coffee tin game on tin
	 *      {@link TextIO#putf(String, Object...)}: print the tin content again if last bean is correct
	 *      {@link TextIO#putf(String, Object...)}: print its color else
	 *      {@link TextIO#putf(String, Object...)}: print an error message
	 * </tt>
	 */
	public static void main(String args[]) {
		char[][] tins = {{ BLUE, BLUE, BLUE, GREEN, GREEN, GREEN }, {BLUE, BLUE, GREEN, GREEN},
				{BLUE, GREEN}, {GREEN,GREEN,GREEN}, {BLUE, BLUE,BLUE,BLUE}};

        for (char[] tin : tins) {

            // count number of greens
            int greens = 0;
            for (char bean : tin) {
                if (bean == GREEN)
                    greens++;
            }

            // p0 = green parity /\
            // (p0=1 -> last=GREEN) /\ (p0=0 -> last=BLUE)
            final char last = (greens % 2 == 1) ? GREEN : BLUE;

            // print the content of tin before the game
            TextIO.putf("tin before: %s %n", Arrays.toString(tin));
            // perform the game
            char lastBean = tinGame(tin);
            // lastBean = last \/ lastBean != last
            // print the content of tin and last bean
            TextIO.putf("tin after: %s %n", Arrays.toString(tin));
            // check if last bean as expected and print
            if (lastBean == last) {
                TextIO.putf("last bean: %c%n", lastBean);
            } else {
                TextIO.putf("Oops, wrong last bean: %c (expected: %c)%n", lastBean, last);
            }
            TextIO.putln();
		}
	}

	/**
	 * Performs coffee tin game to determine the last bean.
	 *
	 * @requires tin is not null /\ tin.length > 0
	 * @modifies tin
	 * @effects <tt>
	 * 		repeat take out two beans from tin
	 * 		if same color
	 * 			throw both away, put one blue bean back
	 * 		else
	 * 			put green bean back until tin has less than 2 beans left
	 * 		let p0 = initial number of green beans
	 * 		if p0 = 1
	 * 			result = 'G'
	 * 		else
	 * 			result = 'B'
	 * </tt>
	 */
	public static char tinGame(char[] tin) {
		while (hasAtLeastTwoBeans(tin)) {
			// take two beans from tin
			char[] takeTwo = takeTwo(tin);
			updateTin(tin, takeTwo);
		}
		return anyBean(tin);
	}

	/**
	 * @effects return a random integer r from the range [0, n)
	 */
	public static int randInt(int n) {
		return (int) (Math.random() * n);
	}

	/**
	 * @effects <tt>
	 * 		if tin has at least two beans
	 * 			return true 
	 * 		else 
	 * 			return false
	 *          </tt>
	 */
	private static boolean hasAtLeastTwoBeans(char[] tin) {
		int count = 0;
		for (char bean : tin) {
			if (bean != REMOVED) {
				count++;
			}
			if (count >= 2) // enough beans
				return true;
		}
		// not enough beans
		return false;
	}

	/**
	 * @requires tin has at least 2 beans left
	 * @modifies tin
	 * @effects <tt>
	 * remove any two beans from tin and return them
	 * i.e. 
	 * char b1 = {@link takeOne(char[])} on tin
	 * char b2 = {@link takeOne(char[])} on tin 
	 * 	result = [b1, b2] </tt>
	 */
	private static char[] takeTwo(char[] tin) {
		char first = takeOne(tin);
		char second = takeOne(tin);
		return new char[] { first, second };
	}

	/**
	 * @requires tin has at least one bean
	 * @modifies tin
	 * @effects remove any bean from tin and return it
	 */
	public static char takeOne(char[] tin) {
		int rand = randInt(tin.length);
		char b = tin[rand];
		while (b == REMOVED) {
			rand = randInt(tin.length);
			b = tin[rand];
		}
		tin[rand] = REMOVED;
		return b;
	}

	/**
	 * Update tin according to the game moves.
	 * 
	 * @requires <tt>tin != null /\ twoBeans != null /\ twoBeans.length=2 /\ 
	 * twoBeans[0], twoBeans[1] in { BLUE, GREEN } </tt>
	 * @modifies <tt>tin</tt>
	 * @effects <tt>let b1 = twoBeans[0], b2 = twoBeans[1] 
	 *    if b1 = b2
	 *      throw both away 
	 *      put a blue bean back
	 *    else
	 *      throw away the blue bean
	 *      put the green one backrandInt
	 *  </tt>
	 */
	public static void updateTin(char[] tin, char[] twoBeans) {
		char b1 = twoBeans[0];
		char b2 = twoBeans[1];

		// process beans to update tin
		if (b1 == b2) {
			b1 = getBean(BeansBag, BLUE);
		} else {
			b1 = getBean(BeansBag, GREEN);
		}
		putIn(tin, b1);
	}

	/**
	 * @modifies BeansBag
	 * @effects <tt>
	 * 		if there are beans that match beanType
	 *      	return a randomly-selected bean among them and remove it from BeansBag
	 *  	else
	 *      	return '\u0000' (null character) </tt>
	 */
	public static char getBean(char[] BeansBag, char beanType) {
		int[] indexArray = new int[BeansBag.length];
		int count = 0;
		for (int i = 0; i < BeansBag.length; i++) {
			if (BeansBag[i] == beanType) {
				indexArray[count] = i;
				count++;
			}
		}

		if (count == 0)
			return NULL; // empty of bean

		int pos = indexArray[randInt(count)];
		char selectedBean = BeansBag[pos];
		BeansBag[pos] = REMOVED;
		return selectedBean;
	}

	/**
	 * @requires tin has vacant positions for new beans
	 * @modifies tin
	 * @effects place bean into any vacant position in tin
	 */
	private static void putIn(char[] tin, char bean) {
		for (int i = 0; i < tin.length; i++) {
			if (tin[i] == REMOVED) { // vacant position
				tin[i] = bean;
				break;
			}
		}
	}

	/**
	 * @effects <tt>
	 * 		if there are beans in tin
	 * 			 return any such bean 
	 * 		else 
	 * 			return '\u0000' (null character)
	 * </tt>
	 */
	private static char anyBean(char[] tin) {
		for (char bean : tin) {
			if (bean != REMOVED) {
				return bean;
			}
		}
		// no beans left
		return NULL;
	}
}
