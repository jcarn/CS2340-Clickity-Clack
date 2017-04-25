from app import app, mysql
from flask import render_template, request, url_for, redirect, flash, session

@app.route('/')
def index():
    if not session.get('logged_in'):
        return redirect('login')
    else:
        return redirect('home')

@app.route('/login', methods = ['GET', 'POST'])
def login():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']

        conn = mysql.connection
        cursor = conn.cursor()

        cursor.execute("SELECT Username FROM User WHERE Username='" + username + "' AND Password='" + password + "';")

        data = cursor.fetchall()
        if len(data) > 0:
            session['logged_in'] = True
            session['user'] = username

            cursor.execute("SELECT * FROM Category_Names;")

            session['categories'] = cursor.fetchall()

            return redirect(url_for('home'))
    return render_template("login.html")

@app.route('/logout')
def logout():
    session.pop('logged_in', None)
    return redirect('login')


@app.route('/home')
def home():
    if not session.get('logged_in'):
        return redirect('login')
    else:
        user = session.get('user')
        name = session.get('name')

        categories = session.get('categories')

        return render_template('home.html', title='home', user=user,
                                categories=categories, name=name)

@app.route('/edit_user', methods = ['GET', 'POST'])
def edit_user():
    if not session.get('logged_in'):
        return redirect('login')
    else:
        conn = mysql.connection
        cursor = conn.cursor()

        user = session.get('user')

        if request.method == 'POST':
            password = request.form['password']
            email = request.form['email']
            cursor.execute(
            """
            UPDATE User
            SET Email = %s, Password = %s
            WHERE Username = %s;
            """, (email, password, user)
            )
            conn.commit()
            return redirect('edit_user')
        else:
            categories = session.get('categories')
            cursor.execute("SELECT * FROM User WHERE Username='" + user + "';")
            userdata = cursor.fetchall()
            
            cursor.execute("SELECT Name, ID FROM Resource WHERE Creator_Username='" + user + "';")
            userresource = cursor.fetchall()
            return render_template('edit_user.html', title = 'edit profile', user = user,
                                   categories = categories, userdata = userdata, userresource = userresource)

@app.route('/searchName/<name>/', methods=["GET", "POST"])
def searchName(name):
    if not session.get('logged_in'):
        return redirect('login')
    else:
        user = session.get('user')
        categories = session.get('categories')

        conn = mysql.connection
        cursor = conn.cursor()

        name = "%" + name + "%"
        cursor.execute("""
                        SELECT rev.rating, res.name, res.description, res.Address_State AS State,
                            res.Address_City AS City, res.Address_Zip AS Zip, res.Address_Street AS Street,
                            res.Address_Number AS Num
                        FROM (
                                SELECT *
                                FROM Resource
                                WHERE name LIKE %s
                            ) res
                        NATURAL LEFT JOIN (
                                SELECT ID, AVG(Rating) AS rating
                                FROM Reviews
                                GROUP BY ID
                            ) rev
                        ORDER BY rev.rating DESC;
                                """, (name, ))
        resources = cursor.fetchall()
        print resources

        return render_template('search.html', resources=resources, categories=categories, user=user)


@app.route('/search/<ctgry>/', methods=["GET"])
def search(ctgry):
    print ctgry
    if not session.get('logged_in'):
        return redirect('login')
    else:
        user = session.get('user')
        categories = session.get('categories')

        conn = mysql.connection
        cursor = conn.cursor()

        cursor.execute("""
                        SELECT rev.rating, res.name, res.description, res.Address_State AS State,
                            res.Address_City AS City, res.Address_Zip AS Zip, res.Address_Street AS Street,
                            res.Address_Number AS Num
                        FROM (
                                SELECT *
                                FROM Resource
                                NATURAL JOIN (
                                    SELECT ID
                                    FROM Categories
                                    WHERE Name = %s
                                    ) categories
                            ) res
                        NATURAL LEFT JOIN (
                                SELECT ID, AVG(Rating) AS rating
                                FROM Reviews
                                GROUP BY ID
                            ) rev
                        ORDER BY rev.rating DESC;
                                """, (ctgry, ))
        resources = cursor.fetchall()
        print resources

        return render_template('search.html', resources=resources, categories=categories, user=user)


@app.route('/resource_detail')
def resource_detail():
    if not session.get('logged_in'):
        return redirect('login')
    
    else:
        user = session.get('user')
        resourcename = request.args['resourcename']

        conn = mysql.connection
        cursor = conn.cursor()

        cursor.execute("SELECT * FROM Resource WHERE Name = '" + resourcename + "';")
        resource = cursor.fetchall()

        id = resource[0][12]
        
        cursor.execute("SELECT Phone_Number FROM Phone_Numbers WHERE ID = %s;", (id,))
        phones = cursor.fetchall()
  
        cursor.execute("SELECT * FROM User_Favorites WHERE Username = %s AND ID = %s;", (user, id, ))
        isfav = cursor.fetchall()

        if len(isfav) > 0:
            favorite = True
        else:
            favorite = False
        
        categories = session.get('categories')
        
        return render_template('resource_detail.html', title='resource details',
                               user = user, categories = categories, resource = resource, phones = phones, favorite = favorite)


@app.route('/deletefav/<resourceid>', methods=['GET'])
def deletefav(resourceid):
    user = session.get('user')
    conn = mysql.connection
    cursor = conn.cursor()

    cursor.execute("""DELETE FROM User_Favorites
                    WHERE Username = %s AND ID = %s;""", (user, resourceid, ))
    conn.commit()
    
    return redirect(url_for('favorites'))


@app.route('/addfav/<resourceid>', methods=['GET'])
def addfav(resourceid):
    user = session.get('user')

    conn = mysql.connection
    cursor = conn.cursor()

    cursor.execute("""INSERT INTO User_Favorites
                        VALUES (%s, %s);""", (user, resourceid, ))
    conn.commit()
    return redirect(url_for('favorites'))


@app.route('/favorites')
def favorites():
    if not session.get('logged_in'):
        return redirect('login')
    else:
        user = session.get('user')
        conn = mysql.connection
        cursor = conn.cursor()

        cursor.execute("""SELECT rev.avg_rating AS Rating, res.name AS Name, res.description AS Description, res.Address_State AS State,
                                res.Address_City AS city, res.Address_Zip AS Zip, res.Address_Street AS Street, res.Address_Number AS Num
                            FROM (
                                SELECT *
                                FROM (
                                    SELECT ID
                                    FROM User_Favorites
                                    WHERE Username = %s
                                    ) favs
                                NATURAL JOIN Resource
                                ) res
                            NATURAL LEFT JOIN (
                                SELECT ID, AVG(Rating) AS avg_rating
                                FROM Reviews
                                GROUP BY ID
                                ) rev
                            ORDER BY rev.avg_rating DESC;""", (user, ))
        resources = cursor.fetchall()

        categories = session.get('categories')
        return render_template('search.html', resources=resources, categories=categories, user=user, favorites = True)


@app.route('/organizations')
def organizations():
    if not session.get('logged_in'):
        return redirect('login')
    else:
        user = session.get('user')
        categories = session.get('categories')

        conn = mysql.connection
        cursor = conn.cursor()

        cursor.execute("SELECT * FROM Organization;")
        orgdata = cursor.fetchall()

        return render_template('organizations.html', title='Organizations', user = user,
                               orgdata = orgdata, categories = categories)


@app.route('/user_detail')
def user_detail():
    user = session.get('user')
    categories = session.get('categories')
    detailorg = request.args['detailorg']

    conn = mysql.connection
    cursor = conn.cursor()

    cursor.execute("SELECT * FROM Organization WHERE Name = '" + detailorg + "';")
    orgdata = cursor.fetchall()

    cursor.execute("SELECT Username FROM User WHERE Organization = '" + detailorg + "';")
    name = cursor.fetchall()
    name = name[0][0]

    cursor.execute("SELECT Name FROM Resource WHERE Creator_Username = '" + name + "';")
    resources = cursor.fetchall()

    return render_template('user_detail.html', title='User Details', user = user,
                           orgdata = orgdata, detailorg = detailorg, categories = categories, resources = resources)



@app.route('/editresource<name>', methods=['GET', 'POST'])
def editresource(name):
    if not session.get('logged_in'):
        return redirect('login')
    else:
        user = session.get('user')

        conn = mysql.connection
        resourceName = name
        cursor = conn.cursor()

        cursor.execute("SELECT * FROM Resource WHERE Name = '" + resourceName + "';")
        resource = cursor.fetchall()
        id = resource[0][12]

        cursor.execute("SELECT Phone_Number FROM Phone_Numbers WHERE ID = %s;", (id,))
        phones = cursor.fetchall()
        
        cursor.execute("SELECT Name FROM Categories WHERE ID = %s;", (id,))
        resource_categories = cursor.fetchall()
        checked_categories = []

        
        for category in resource_categories:
            if category[0] == 'Childcare':
                checked_categories.append('Childcare')
            elif category[0] == 'Education':
                checked_categories.append('Education')
            elif category[0] == 'Employment':
                checked_categories.append('Employment')
            elif category[0] == 'For_Children':
                checked_categories.append('For_Children')
            elif category[0] == 'Housing':
                checked_categories.append('Housing')
            elif category[0] == 'Job_Readiness':
                checked_categories.append('Job_Readiness')
            elif category[0] == 'Legal':
                checked_categories.append('Legal')
            elif category[0] == 'Life_Skills':
                checked_categories.append('Life_Skills')
            elif category[0] == 'Medical':
                checked_categories.append('Medical')
            elif category[0] == 'Mental_Health':
                checked_categories.append('Mental_Health')
            elif category[0] == 'Mentors':
                checked_categories.append('Mentors')
            elif category[0] == 'Networks':
                checked_categories.append('Networks')
            elif category[0] == 'Supplies':
                checked_categories.append('Supplies')
            elif category[0] == 'Transportation':
                checked_categories.append('Transportation')
            elif category[0] == 'Vehicle':
                checked_categories.append('Vehicle')

        if request.method == 'POST':
            resourceName = request.form['resourceName']
            resourcePhone = request.form.getlist('resourcePhone')
            resourceStreet = request.form['resourceStreet']
            resourceCity = request.form['resourceCity']
            resourceWebsite = request.form['resourceWebsite']
            streetsplit = resourceStreet.split(" ");
            streetNum = streetsplit[0]
            streetName = " ".join(map(str,streetsplit[1:]))
            resourceState = request.form['resourceState']
            resourceZip = request.form['resourceZip']
            resourceEligibility = request.form['resourceEligibility']
            resourceDescription = request.form['resourceDescription']
            resourceCategories = request.form.getlist('checkedCategory')

            non_citizens = request.form.getlist('takes_non_citizens')
            need_id = request.form.getlist('id')
            if len(non_citizens) > 0:
                resourceNonCitizen = 1
            else:
                resourceNonCitizen = 0
            if len(need_id) > 0:
                resourceDocumentation = 1
            else:
                resourceDocumentation = 0

            cursor.execute("""UPDATE Resource
                                SET Name = %s, Creator_Username = %s, Address_State = %s, Address_City = %s, Address_Zip = %s,
                                 Address_Street = %s, Address_Number = %s, Website = %s, Non_Citizen = %s, Documentation = %s,
                                 Eligibility = %s, Description = %s
                                WHERE ID = %s;""",
                                (resourceName, user, resourceState, resourceCity, resourceZip, streetName, streetNum, resourceWebsite, resourceNonCitizen, resourceDocumentation, resourceEligibility, resourceDescription, id,))

            conn.commit()

            cursor.execute("DELETE FROM Phone_Numbers WHERE ID = %s;", (id,))
            conn.commit()

            for phone in resourcePhone:
                #insert edits
                cursor.execute("INSERT INTO Phone_Numbers (Phone_Number, ID) VALUES (%s, %s);", (phone, id))
                conn.commit()


            for category in resourceCategories:
                if category not in resource_categories:
                    print("to insert: ", category)

            for category in resource_categories:
                if category not in resourceCategories:
                    print("to delete: ", category)


            if 'Childcare' in request.args:
                cminage = request.form['childcare-min-age']
                cmaxage = request.form['childcare-max-age']
                cmincost = request.form['childcare-min-cost']
                cmaxcost = request.form['childcare-max-cost']
                ctype = request.form['childcare-type']
                cursor.execute("SELECT * FROM Childcare WHERE ID = %s;", (id,))
                childcareCheck = cursor.fetchall()

                if len(childcareCheck) > 0:
                    #update
                    cursor.execute("UPDATE Childcare SET AgeMax = %s, AgeMin = %s, CostMax = %s, CostMin = %s WHERE ID = %s;", (cmaxage, cminage, cmaxcost, cmincost, id,))
                    conn.commit()

                    cursor.execute("SELECT Child_Type FROM Child_Type WHERE ID = %s;", (id,))
                    child_typecheck = cursor.fetchall()

                    for x in child_typecheck:
                        if x not in ctype:
                            #delete type
                            cursor.execute("DELETE FROM Child_Type WHERE Child_Type = %s AND ID = %s", (x, id,))
                            conn.commit()

                    for y in ctype:
                        if y not in child_typecheck:
                            #insert type
                            cursor.execute("INSERT INTO Child_Type VALUES (%s, %s);", (y, id,))
                            conn.commit()     

  
                else:
                    #create new
                    cursor.execute("INSERT INTO Childcare (AgeMax, AgeMin, CostMax, CostMin, ID) VALUES (%s, %s,  %s, %s, %s);", (cmaxage, cminage, cmaxcost, cmincost, id,))
                    conn.commit()

                    for x in ctype:
                        cursor.execute("INSERT INTO Child_Type VALUES (%s, %s);", (x, id,))
                        conn.commit()

            if 'Education' in request.args:
                emincost = request.form['education-min-cost']
                emaxcost = request.form['education-max-cost']
                eprereqs = request.form['education-prereqs']
                etype = request.form['education-type']

                cursor.execute("SELECT * FROM Education WHERE ID = %s;", (id,))
                educationCheck = cursor.fetchall()

                if len(educationCheck) > 0:
                    #update
                    cursor.execute("UPDATE Education SET CostMax = %s, CostMin = %s, Prerequisites = %s WHERE ID = %s;", (emaxcost, emincost, eprereqs, id,))
                    conn.commit()

                    cursor.execute("SELECT Education_Type FROM Education_Type WHERE ID = %s;", (id,))
                    edutypecheck = cursor.fetchall()

                    for x in edutypecheck:
                        if x not in etype:
                            cursor.execute("DELETE FROM Education_Type WHERE Education_Type = %s AND ID = %s;", (x, id,))
                            conn.commit()
                    for y in etype:
                        if y not in edutypecheck:
                            cursor.execute("INSERT INTO Education_Type VALUES (%s, %s);", (y, id,))
                            conn.commit()

                else:
                    #create new
                    cursor.execute("INSERT INTO Education VALUES (%s, %s,  %s, %s);", (emaxcost, emincost, eprereqs, id,))
                    conn.commit()

                    for x in etype:
                        cursor.execute("INSERT INTO Education_Type VALUES (%s, %s);", (x, id,))
                        conn.commit()

            if 'Employment' in request.args:
                eminsalary = request.form['employment-min-salary']
                emaxsalary = request.form['employment-max-salary']
                emptype = request.form['emp-type']
                empskills = request.form['emp-skills']
                empchildcare = request.form['childcare']

                cursor.execute("SELECT * FROM Employment WHERE ID = %s;", (id,))
                employmentcheck = cursor.fetchall();

                if len(employmentcheck) > 0:
                    #update
                    cursor.execute("UPDATE Employment SET SalaryMax = %s, SalaryMin = %s, Childcare = %s WHERE ID = %s;", (emaxsalary, eminsalary, empchildcare, id,))
                    conn.commit()

                    cursor.execute("SELECT Emp_Type FROM Emp_Type WHERE ID = %s;", (id,))
                    emptypecheck = cursor.fetchall();

                    for x in emptypecheck:
                        if x not in emptype:
                            cursor.execute("DELETE FROM Emp_Type WHERE Emp_Type = %s AND ID = %s;", (x, id,))
                            conn.commit()
                    for y in emptype:
                        if y not in emptypecheck:
                            cursor.execute("INSERT INTO Emp_Type VALUES (%s, %s);", (y, id,))
                            conn.commit()

                    cursor.execute("SELECT Emp_Skills FROM Emp_Skills WHERE ID = %s;", (id,))
                    empskillcheck = cursor.fetchall();

                    for x in empskillcheck:
                        if x not in empskills:
                            cursor.execute("DELETE FROM Emp_Skills WHERE Emp_Skills = %s AND ID = %s;", (x, id,))
                            conn.commit()
                    for y in empskills:
                        if y not in empskillcheck:
                            cursor.execute("INSERT INTO Emp_Skills VALUES (%s, %s);", (y, id,))
                            conn.commit()

  
                else:
                    #create new
                    cursor.execute("INSERT INTO Employment VALUES (%s, %s, %s, %s);", (emaxsalary, eminsalary, empchildcare, id,))
                    conn.commit()

                    for x in emptype:
                        cursor.execute("INSERT INTO Emp_Type VALUES (%s, %s);", (x, id,))
                        conn.commit()

                    for y in empskills:
                        cursor.execute("INSERT INTO Emp_Skills VALUES (%s, %s);", (y, id,))
                        conn.commit()


            if 'For_Children' in request.args:
                fcminage = request.form['for-children-min-age']
                fcmaxage = request.form['for-children-max-age']
                fctype = request.form['for-children-type']

                cursor.execute("SELECT * FROM For_Children WHERE ID = %s;", (id))
                For_Child_Check = cursor.fetchall()

                if len(For_Child_Check) > 0:
                    #update
                    cursor.execute("UPDATE For_Childrem SET AgeMax = %s, AgeMin = %s WHERE ID = %s;", 
                        fcmaxage, fcminage, id)
                    conn.commit()

                    cursor.execute("SELECT For_Child FROM For_Child WHERE ID = %s;", (id,))
                    for_Child_typecheck = cursor.fetchall()

                    for x in for_Child_typecheck:
                        if x not in fctype:
                            cursor.execute("DELETE FROM For_Child WHERE For_Child = %s AND ID = %s;", (x, id,))
                            conn.commit()
                    for y in fctype:
                        if y not in for_Child_typecheck:
                            cursor.execute("INSERT INTO Child_Type VALUES (%s, %s);", (y, id,))
                            conn.commit()
                else:
                    #create new
                    cursor.execute("INSERT INTO For_Children VALUES (%s, %s, %s);", (fcmaxage, fcminage, id,))
                    conn.commit()

                    for x in fctype:
                        cursor.execute("INSERT INTO For_Child VALUES (%s, %s);", (x, id,))


            if 'Housing' in request.args:
                hcapacity = request.form['housing-capacity']
                hgender = request.form['housing-gender']
                hminage = request.form['housing-min-age']
                hmaxage = request.form['housing-max-age']
                htype = request.form['housing-type']
                hserves = request.form['housing-serves']
                hchildren = request.form['takesChildren']


                cursor.execute("SELECT * FROM Housing WHERE ID = %s;", (id,))
                housingcheck = cursor.fetchall();

                if len(housingcheck) > 0:
                    #update
                    cursor.execute("UPDATE Housing SET Capacity = %s, Gender = %s, AgeMax = %s, AgeMin = %s, Children = %s WHERE ID = %s;", (hcapacity, hgender, hmaxage, hminage, hchildren, id,))
                    conn.commit()

                    cursor.execute("SELECT Housing_Type FROM Housing_Type WHERE ID = %s;", (id,))
                    housetypecheck = cursor.fetchall();

                    for x in housetypecheck:
                        if x not in htype:
                            cursor.execute("DELETE FROM Housing_Type WHERE Housing_Type = %s AND ID = %s;", (x, id,))
                            conn.commit()
                    for y in htype:
                        if y not in housetypecheck:
                            cursor.execute("INSERT INTO Housing_Type VALUES (%s, %s);", (y, id,))
                            conn.commit()

                    cursor.execute("SELECT Housing_Serve FROM Housing_Serve WHERE ID = %s;", (id,))
                    houseservecheck = cursor.fetchall();

                    for x in houseservecheck:
                        if x not in hserves:
                            cursor.execute("DELETE FROM Housing_Serve WHERE Housing_Serve = %s AND ID = %s;", (x, id,))
                            conn.commit()
                    for y in hserves:
                        if y not in houseservecheck:
                            cursor.execute("INSERT INTO Housing_Serve VALUES (%s, %s);", (y, id,))
                            conn.commit()
  
                else:
                    #create new
                    cursor.execute("INSERT INTO Employment VALUES (%s, %s, %s, %s);", (emaxsalary, eminsalary, empchildcare, id,))
                    conn.commit()

                    for x in emptype:
                        cursor.execute("INSERT INTO Emp_Type VALUES (%s, %s);", (x, id,))
                        conn.commit()

                    for y in empskills:
                        cursor.execute("INSERT INTO Emp_Skills VALUES (%s, %s);", (y, id,))
                        conn.commit()

            if 'Job_Readiness' in request.args:
                jrtraining = request.form['Training']
                jrcounseling = request.form['Counseling']

            if 'Legal' in request.args:
                legtype = request.form['legal-type']

            if 'Life_Skills' in request.args:
                lscostmin = request.form['life-skills-min-cost']
                lscostmax = request.form['life-skills-max-cost']
                lstype = request.form['life-skills-type']

            if 'Medical' in request.args:
                medinsurance = request.form['medical-insurance']
                medtype = request.form['medical-type']

            if 'Mental_Health' in request.args:
                mentalinsurance = request.form['mental-health-insurance']
                mentaltype = request.form['mental-type']

            if 'Mentors' in request.args:
                mentorcostmin = request.form['mentors-min-cost']
                mentorcostmax = request.form['mentors-max-cost']
                mentortype = request.form['mentor-type']

            if 'Networks' in request.args:
                netmale = request.form['Male']
                netfemale = request.form['Female']
                netall = request.form['All']
                netother = request.form['Other']
                netagemin = request.form['networks-min-age']
                netagemax = request.form['networks-max-age']
                netmem = request.form['network-members']
                netsub = request.form['network-subject']

            if 'Supplies' in request.args:
                supptype = request.form['supply-type']
                suppcostmin = request.form['supply-min-cost']
                suppcostmax = request.form['supply-max-cost']

            if 'Transportation' in request.args:
                transcostmin = request.form['transportation-min-cost']
                transcostmax = request.form['transportation-max-cost']
                transtype = request.form['transp-type']

            if 'Vehicle' in request.args:
                vehiclecostmin = request.form['vehicle-min-cost']
                vehiclecostmax = request.form['vehicle-max-cost']
                vehicletype = request.form['vehicle-type']

            return redirect(url_for('edit_user'))
    categories = session.get('categories')
    print(categories[1][0] in checked_categories)
    return render_template('edit_add_resource.html', title = "Edit Resource", user = user,
                           categories = categories, resource = resource, phones = phones, checked_categories = checked_categories)

@app.route('/addresource', methods = ['GET', 'POST'])
def addresource():
    if not session.get('logged_in'):
        return redirect('login')
    else:
        user = session.get('user')
        conn = mysql.connection
        cursor = conn.cursor()
        if request.method == 'POST':
            resourceName = request.form['resourceName']
            resourceState = request.form['resourceState']
            resourceCity = request.form['resourceCity']
            resourceZip = request.form['resourceZip']
            resourceStreet = request.form['resourceStreet']
            resourceWebsite = request.form['resourceWebsite']
            resourceDescription = request.form['resourceDescription']
            resourcePhone = request.form.getlist('resourcePhone')
            streetsplit = resourceStreet.split(" ");
            streetNum = streetsplit[0]
            streetName = " ".join(map(str,streetsplit[1:]))
            non_citizens = request.form.getlist('takes_non_citizens')
            need_id = request.form.getlist('id')
            if len(non_citizens) > 0:
                resourceNonCitizen = 1
            else:
                resourceNonCitizen = 0
            if len(need_id) > 0:
                resourceDocumentation = 1
            else:
                resourceDocumentation = 0
            resourceEligibility = request.form['resourceEligibility']

            resourceCategories = request.form.getlist('checkedCategory')
            cursor3 = conn.cursor()
            # still need to check that certain fields aren't null
            # still need to fix radio buttons
            # still need to do sub categories
            cursor.execute("""INSERT INTO Resource (Name, Creator_Username, Address_State, Address_City,
                Address_Zip, Address_Street, Address_Number, Website, Non_Citizen, Documentation, Eligibility,
                Description) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);""", 
                (resourceName, user, resourceState, resourceCity, resourceZip, streetName, streetNum,
                    resourceWebsite, resourceNonCitizen, resourceDocumentation, resourceEligibility, resourceDescription,))
            conn.commit()
            cursor5 = conn.cursor()
            cursor5.execute("SELECT ID FROM Resource WHERE Name='" + resourceName + "';")
            ids = cursor5.fetchall()
            id = ids[0][0]
            cursor4 = conn.cursor()
            cursor6 = conn.cursor()
            for phone in resourcePhone:
                cursor4.execute(""" INSERT INTO Phone_Numbers
                VALUES (%s, 'Cell', %s)""", (phone, id,))
                conn.commit()
            for category in resourceCategories:
                cursor6.execute("""INSERT INTO Categories
                VALUES (%s, %s);""", (category, id,))
                conn.commit()
            return redirect(url_for('edit_user'))

    categories = session.get('categories')
    return render_template('edit_add_resource.html', title = "Add Resource", user = user,
                           categories = categories)

@app.route('/deleteresource<id>', methods=['GET'])
def deleteresource(id):
    conn = mysql.connection
    cursor = conn.cursor()
    cursor.execute("DELETE FROM Resource WHERE ID = %s;", (id,))
    conn.commit()

    return redirect(url_for('edit_user'))

