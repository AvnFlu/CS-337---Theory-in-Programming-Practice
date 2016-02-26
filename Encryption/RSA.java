import java.util.Scanner;
import java.io.*;

/*
*  RSA works by taking a number and exponentiating it with the public key value and finding the
*  modulo of that value using modulus n, producing an encyrpted number.
*  c = (m^e) % n
*
*  To decrypt the same method is applied using the private key value as an exponent for the
*  encrypted number, then using the modulus n to get the modulo, which will be the original number.
*  m = (c^d) % n
*/

public class RSA {
	public static void main(String[] args) throws IOException {

		File path;             // used to grab files out of the current directory
		FileInputStream in;    // reads from the file the path provided

		int n;
		int e;
		int d;

		//grab the input files
		try {
			if(args.length < 1)
			{
				System.out.println("Enter a file to create \"encrypted\".\nEnter \"encrypted\" to decrypt to original format");
				Scanner sc = new Scanner(System.in);
				path = new File(sc.next());
			}
			else
		        path = new File(args[0]);

			in = new FileInputStream(path);

			// "key.txt" should be in the same directory after being created by KeyGenerator.java
			File k = new File("key.txt");  
			Scanner key = new Scanner(k);

			n = key.nextInt();
			e = key.nextInt();
			d = key.nextInt();
		}
		catch(FileNotFoundException f) {
			System.out.println("File Not Found");
			return;
		}

		//encrypt or decrypt the provided file based on its name
		if(path.getName().equals("encrypted")) {
			FileOutputStream out = new FileOutputStream("decrypted");
			Decrypt(d, n, in, out);
			System.out.println("\n\"decrpyted\" created");
		}
		else {
            FileOutputStream out = new FileOutputStream("encrypted");
            Encrypt(e, n, in, out);
            System.out.println("\n\"encrpyted\" created");
		}
	}


	public static void Encrypt(int e, int n, FileInputStream in, FileOutputStream out) throws IOException {

		//take in a block of 3 bytes from the input and encrypt them until there is
		//nothing else left to take in
		int size = in.available();
		for(int i = 0; i < size; i += 3) {
			//read 3 bytes into an array
			byte[] byts = new byte[4];
			in.read(byts, 1, 3);

			//convert that block into a long
			//'&' will negate all negative values the bytes may hold when combining
			long m = ((65536*byts[1])&16711680)
			 + ((256*byts[2])&65280)
			 + ((1*(byts[3]))&255);


			//calculate (m^e)*(mod(n))
			int c = Modular(e, n, m);

			//divide the encrypted int into a 4 byte array
			byte[] outByts = new byte[4];
			for(int j = 3; j >= 0; j--) {
				outByts[j] = (byte)(c % 256);
				c /= 256;	
			}

			//write that block into a new file
			out.write(outByts, 0, 4);
		}
	}


	public static void Decrypt(int d, int n, FileInputStream in, FileOutputStream out) throws IOException {
		int size = in.available();
	    for(int i = 0; i < size; i += 4) {
			//read 4 bytes into an array
	        byte[] byts = new byte[4];
	        in.read(byts, 0, 4);

			//convert that block into a long
			//'&' will negate all negative values the bytes may hold when combining
	        long c = ((16777216*byts[0]))
				 + ((65536*byts[1])&16711680)
				 + ((256*byts[2])&65280)
				 + ((1*(byts[3]))&255);


	        //calculate (c^d)*(mod(n))
	        int m = Modular(d, n, (long)c);


			//divide the decrytped int into a 4 byte array
			byte[] outByts = new byte[4];
			for(int j = 3; j >= 0; j--) {
				outByts[j] = (byte)(m % 256);
				m /= 256;
			}
				
			//write the 3 valuable bytes to a new file
			for(int j = 1; j < 4; j++) {
				//break in the event of a null terminator
				if(outByts[j] == 0)
					break;

				out.write((char)(outByts[j]));
			}
		}
	}


	/* This method uses the memory-efficient method of modular exponentiation to avoid extraordinarily large numbers */
	public static int Modular(int e, int n, long l)
	{
		//determine the number of binary digits for e
		//j will be the power of 2
		int j = 1;
		//size of the array
		int size = 0;
		while(j <= e) {
			size++;
			j *= 2;
		}

		j /= 2;
		
		//this array will hold the binary digits for e
		boolean[] eList = new boolean[size]; 
		
		//place the digits of e (as booleans) in the array
		for(int i = 0; e > 0; i++) {
			//if e can be subtracted by 2^x
			if(e - j >= 0) {
				//store a 1 in the digit's place and remove that from e 
				eList[i] = true;
				e -= j;
			}
			else
				//store a 0 in the digit's place
				eList[i] = false;

			j /= 2;
		}

		//modular exponentiation
		long c = 1;
		for(int i = 0; i < size; i++) {
			if(!eList[i]) 
				c = ((c*c) % n);
			else 
				c = ((((c*c) % n) * l) % n);
		}

		return (int)c;
	}
}
