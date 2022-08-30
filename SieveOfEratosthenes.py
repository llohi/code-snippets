'''
Following is the algorithm to find all the prime numbers less than or equal to a given integer n by the Eratosthene’s method: 
When the algorithm terminates, all the numbers in the list that are not marked are prime.

More info:
https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
'''

def SieveOfEratosthenes(n):
    
    # create boolean array of True values
    prime = [True for i in range(n + 1)]
    p = 2

    while p * p <= n:

        # if prime number, update all multiples to false
        if prime[p] == True:

            for i in range(p * p, n + 1, p):
                prime[i] = False
        
        p += 1

    # print all primes
    for p in range(2, n + 1):
        if prime[p]:
            print(p)

def main():

    n = int(input("Enter n: "))
    SieveOfEratosthenes(n)
    


if __name__ == "__main__":
    main()