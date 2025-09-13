

# pip install mysql
# pip install mysql-connector-python
# pip install tabulate
# https://www.geeksforgeeks.org/introduction-to-python-tabulate-library/
# https://stackoverflow.com/questions/71707258/tabulating-data-coming-from-a-db-query
# https://www.youtube.com/watch?v=oDR7k66x-AU

import mysql.connector as mariadb
from tabulate import tabulate

'''try:
    with mariadb.connect(
            user='root', 
            password='p',
            database='carlog',
            host='localhost',
            port= '3306',
            charset='utf8mb4',
            collation='utf8mb4_general_ci'
        ) as conn:

        cur = conn.cursor()
        #cur.execute('SHOW TABLES;')
        #print('Tables:')
        #for table in cur:
        #   print(table)
        while True:
            nummer = input("Registreringsnummer: ").replace(" ", "").lower()
            if len(nummer) == 6:
                break
            else:
                print('The registreringsnumber must be only 6 characters.Try again.')
                

        sql_statement = "SELECT * FROM carlog_db WHERE regnr = %s ; "
        cur.execute(sql_statement, (nummer,))
        my_result = cur.fetchall() #fetchone
        if my_result:
            print("Information:")
            for result in my_result :
                print(result)
        else:
            print(f"No results found for registreringsnummer: {nummer}")

        cur.close()
except mariadb.Error as e:
    print(f'{e}')'''

try:
    with mariadb.connect(
            user='root', 
            password='p',
            database='carlog',
            host='localhost',
            port= '3306',
            charset='utf8mb4',
            collation='utf8mb4_general_ci'
        ) as conn:

        cur = conn.cursor()
        sql_statement = "SELECT * FROM carlog_db "
        cur.execute(sql_statement,)
        my_result = cur.fetchall() 
        if my_result:
            print("Information:")
            print(tabulate(my_result, headers=["regnr","make","model","color","current_place","future_place",'pickup_date_time','delivery_date_time',"pickup_status","company_name"], tablefmt= "sql" ))

        cur.close()
except mariadb.Error as e:
    print(f'{e}')
