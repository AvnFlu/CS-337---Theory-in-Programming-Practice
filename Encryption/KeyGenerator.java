import java.io.*;

/*  Creates a few simple RSA compatible keys  */

public class KeyGenerator
{
	public static void main(String[] args) throws IOException
	{
		int p;
		int q;

		if(args.length > 1) {
			try {
				p = Integer.parseInt(args[0]);
				q = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException e) {
				System.out.println("Entered arguments are not in integer format");
				return;
			}
		}
		else {
			p = 4100;      // preset seeds, you can change these to whatever value you like,
			q = 6000;      // recommend setting these to large numbers, at least 4 digits
		}

		if(q < 3){
			System.out.println("Second value is too low");
			return;
		}



		while(!isPrime(p))  p++;     // find the first prime number larger than p
		while(!isPrime(q))	q--;     // find the first prime number smaller than q
		
		if(p < 256 || q < 256) {
			System.out.println("Please use seeds much larger than 256");
			return;
		}



		int n = p*q;
		int phiN = (p-1)*(q-1);
		int e = 67;                  // preset public key, changing this will greatly increase the runtime
		int d = 1;

		//determine the value for private key, d
		while((d*e)% phiN != 1)
			d++;

		System.out.println("n: " + n);
		System.out.println("e: " + e);
		System.out.println("d: " + d);
		
		System.out.println("\np: " + p);
		System.out.println("q: " + q);
		System.out.println("phi(n): " + phiN);
		
		FileWriter out = new FileWriter("key.txt");
		out.write(Integer.toString(n)+'\n' +Integer.toString(e)+'\n' +Integer.toString(d));
		out.close();
		System.out.println("\n\"key.txt\" created");
	}
	
	public static boolean isPrime(int n) {
		for(int i = 2; i <= n/2; i++)
			if(n%i==0)
				return false;

		return true;
	}
}
