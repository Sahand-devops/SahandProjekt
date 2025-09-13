import mysql.connector as mariadb
from tabulate import tabulate

# Function to handle user login
def user_login():
    print("Welcome to the Car Log System")
    print("Username: admin| Password: admin123")
    print("Username: driver| Password driver123")
    while True:
        username = input("Enter username: ")
        password = input("Enter password: ")

        # Simulated user roles (in a real-world application, you would query this from a database)
        if username == "admin" and password == "admin123":
            return "admin"
        elif username == "driver" and password == "driver123":
            return "driver"
        else:
            print("Invalid credentials. Please try again.")

# Function to display car log info for a specific regnr
def display_car_info(cursor, regnr):
    sql_statement = "SELECT * FROM carlog_db WHERE regnr = %s"
    cursor.execute(sql_statement, (regnr,))
    my_result = cursor.fetchall()
    if my_result:
        print("Information:")
        print(tabulate(my_result, headers=["regnr", "make", "model", "color", "current_place", "future_place", 'pickup_date_time', 'delivery_date_time', "pickup_status", "company_name"], tablefmt="sql"))
    else:
        print(f"No car logs found for registreringsnummer: {regnr}.")

# Admin functionality
def admin_actions(cursor, regnr):
    regnr = input("Ange registreringsnummer för bilen du vill uppdatera: ").strip().upper()
    print("Vad vill du uppdatera?")
    print("1. Färg")
    print("2. Nuvarande plats")
    print("3. Framtida plats")
    print("4. Upphämtningstid")
    print("5. Leveransdatum")
    print("6. Status")

    choice = input("Välj alternativ: ").strip()

    if choice == "1":
        new_color = input("Ange ny färg: ").strip()
        cursor.execute("UPDATE carlog_db SET color = %s WHERE regnr = %s;", (new_color, regnr))
    elif choice == "2":
        new_current_place = input("Ange ny nuvarande plats: ").strip()
        cursor.execute("UPDATE carlog_db SET current_place = %s WHERE regnr = %s;", (new_current_place, regnr))
    elif choice == "3":
        new_future_place = input("Ange ny framtida plats: ").strip()
        cursor.execute("UPDATE carlog_db SET future_place = %s WHERE regnr = %s;", (new_future_place, regnr))
    elif choice == "4":
        new_pickup_date_time = input("Ange ny upphämtningstid (YYYY-MM-DD HH:MM:SS): ").strip()
        cursor.execute("UPDATE carlog_db SET pickup_date_time = %s WHERE regnr = %s;", (new_pickup_date_time, regnr))
    elif choice == "5":
        new_delivery_date_time = input("Ange nytt leveransdatum (YYYY-MM-DD HH:MM:SS): ").strip()
        cursor.execute("UPDATE carlog_db SET delivery_date_time = %s WHERE regnr = %s;", (new_delivery_date_time, regnr))
    elif choice == "6":
        # Statusval via siffra
        print("Välj en ny status:")
        status_options = {1: "Pending", 2: "In Progress", 3: "Completed"}
        for key, value in status_options.items():
            print(f"{key}: {value}")

        while True:
            try:
                status_choice = int(input("Ange numret för den nya statusen: "))
                if status_choice in status_options:
                    new_status = status_options[status_choice]
                    cursor.execute("UPDATE carlog_db SET pickup_status = %s WHERE regnr = %s;", (new_status, regnr))
                    print(f"Statusen har uppdaterats till {new_status}.")
                    break
                else:
                    print("Ogiltigt val. Välj ett giltigt nummer.")
            except ValueError:
                print("Ange ett nummer.")
    else:
        print("Ogiltigt val.")
        return


# Chauffeur functionality
def driver_actions(cursor, regnr):
    print("Driver Actions: You can only change status.")
    print("Select a new status:")
    status_options = {1: "Pending", 2: "Completed", 3: "In Progress", 4: "Canceled"}
    for key, value in status_options.items():
        print(f"{key}: {value}")
    while True:
        try:
            status_choice = int(input("Enter the number corresponding to the new status: "))
            if status_choice in status_options:
                new_status = status_options[status_choice]
                cursor.execute("UPDATE carlog_db SET pickup_status = %s WHERE regnr = %s", (new_status, regnr))
                print(f"Status updated to {new_status}")
                break
            else:
                print("Invalid choice. Please select a valid number.")
        except ValueError:
            print("Please enter a number.")

# Main program
def main():
    try:
        conn = mariadb.connect(
            user="root",
            password="password1",
            database="carlog_db",
            host="127.0.0.1",
            port="3306",
            charset="utf8mb4",
            collation="utf8mb4_general_ci",
        )
        cur = conn.cursor()

        # User login (only happens once)
        role = user_login()

        while True:
            # Get car registration number from user
            cur.execute("SELECT regnr FROM carlog_db")
            print("Available registreringsnummer: ")
            for regnr in cur:
                print(f"{regnr[0]}")  # regnr is a tuple, so we print the first element
            nummer = input("Registreringsnummer: ").replace(" ", "").lower()

            # Display car information for the specific registreringsnummer
            display_car_info(cur, nummer)

            # Role-based actions
            if role == "admin":
                admin_actions(cur, nummer)
            elif role == "driver":
                driver_actions(cur, nummer)

            # Commit changes
            conn.commit()

            # Ask if the user wants to continue after completing actions
            continue_program = input("Do you want to perform another action? (yes/no): ").lower()
            if continue_program != "yes":
                print("Logging out. Goodbye!")
                break

        # Close the connection
        cur.close()
        conn.close()

    except mariadb.Error as e:
        print(f"Error: {e}")

# Run the program
if __name__ == "__main__":
    main()