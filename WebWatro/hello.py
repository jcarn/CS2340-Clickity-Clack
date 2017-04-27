from flask import *
from flask_googlemaps import GoogleMaps
from flask_googlemaps import Map
import firebase_handler
from model import *

curr_user = None

db = firebase_handler.Database()
app = Flask(__name__)

app.config['GOOGLEMAPS_KEY'] = "AIzaSyCPOAyJ7sYZgkBaeHKdkzJirKox8QLJs0U"
app.config['TEMPLATES_AUTO_RELOAD'] = True
GoogleMaps(app)

@app.route("/")
def mainThing():
    # return "Hello World!"
    return render_template("home.html")

@app.route("/main", methods = ['GET', 'POST'])
# def mapHome():
#     return render_template("mapHome.html")
def main():
# creating a map in the view
    # user = str(db.users[0].firstName)
    global curr_user
    curr_user = db.current_user # do this to get the first name properly
    first_name = curr_user.firstName
    print("asdf " + first_name)
    reports = db.reports
    markerList = []
    
    for report in reports:
        markerList.append(mapMarkers(report))

    mymap = Map(
    identifier="map",
    lat=26,
    lng=-37,
    zoom = 2,
    style="height:500px;width:500px",
    markers=markerList
    )

    if request.method == 'POST':
        return redirect(url_for('editProf'))

    return render_template('mapHome.html', mymap=mymap, name=first_name)

#makes dictionary with marker attributes from a single report - to be added to marker list
def mapMarkers(report):
    locationDict = {}
    info = str(report.waterType) + ", " + str(report.waterCondition)
    locationDict['icon'] = '/static/images/minimarker.png'
    locationDict['lat'] = report.latitude
    locationDict['lng'] = report.longitude
    locationDict['infobox'] = info

    return locationDict


@app.route("/login", methods = ['GET', 'POST'])
def login():
    global curr_user
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']
        db.login(username, password)

        curr_user = db.current_user

        return redirect(url_for('main'))
    return render_template("login.html")

@app.route("/register", methods = ['GET', 'POST'])
def register():
    global curr_user
    if request.method == 'POST':
        # get fields from UI
        firstname = request.form['firstname']
        lastname = request.form['lastname']
        address = request.form['address']
        email = request.form['email']
        password = request.form['password']
        usertype = request.form.get('usertype')

        # create new user to pass into db
        newUser = user.User([email, 0, firstname, lastname, usertype, address])
        db.register_user(newUser, password)

        # set the userFirstName
        curr_user = db.current_user

        # redirect to the home page
        return redirect(url_for('main'))
    return render_template("register.html")

@app.route("/editprofile", methods = ['GET', 'POST'])
def editProf():
    global curr_user
    curr_user = db.current_user # do this to get the user information properly
    fname = curr_user.firstName
    lname = curr_user.lastName
    home = curr_user.homeAddress
    email = curr_user.email
    usertype = curr_user.userType
    print(str(usertype))

    sel1 = ""
    sel2 = ""
    sel3 = ""
    sel4 = ""

    if str(usertype) == "User":
        sel1 = "selected"
    elif str(usertype) == "Worker":
        sel2 = "selected"
    elif str(usertype) == "Manager":
        sel3 = "selected"
    elif str(usertype) == "Administrator":
        sel4 = "selected"

    if request.method == 'POST':
        firstname = request.form['firstname']
        lastname = request.form['lastname']
        address = request.form['address']
        usertype = request.form.get('usertype')
        print(firstname)
        editedUser = user.User([email, curr_user.id, firstname, lastname, usertype, address])
        db.update_object(editedUser)
        return redirect(url_for('main'))

    return render_template("editprofile.html", fname=fname, lname=lname, home=home, email=email, usertype=usertype, sel1=sel1, sel2=sel2, sel3=sel3, sel4=sel4)

@app.route("/source")
def source():
    reports = db.reports
    reportList = []

    for report in reports:
        reportList.append(formatSourceRpt(report))

    return render_template("source.html", reports=reportList)

def formatSourceRpt(report):
    rptID = report.reportID
    date = report.reportDate
    reporter = db.find_user(report.reporterID).firstName
    location = report.streetAddress
    waterType = report.waterType
    waterCond = report.waterCondition
    # pOpen = "<p style='font-size: 15px'>"
    # pClose = "</p>"

    viewList = []

    line1 = "Report #: " + str(rptID) + " | Report Date: " + str(date) + " | Reporter: " + str(reporter) + "\n"
    line2 = "Location: " + str(location) + "\n"
    line3 = "Water Type: " + str(waterType) + " | Water Condition: " + str(waterCond)

    viewList.append(line1)
    viewList.append(line2)
    viewList.append(line3)

    return viewList

@app.route("/newwatersource", methods=['GET','POST'])
def newSource():

    return render_template("newSource.html")

if __name__ == "__main__":
    app.run()