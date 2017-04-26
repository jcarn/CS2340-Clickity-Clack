from flask import *
from flask_googlemaps import GoogleMaps
from flask_googlemaps import Map
import firebase_handler
from model import user

curr_user = ""

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
    first_name = curr_user.firstName
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
    #'firstname' 'lastname' 'address' 'email' 'password' 'usertype' 'registerSubmit'
    return render_template("register.html")

@app.route("/editprofile", methods = ['GET', 'POST'])
def editProf():
    global curr_user
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

    # if method == 'POST':
        #insert edit profile functionality
    return render_template("editprofile.html", fname=fname, lname=lname, home=home, email=email, usertype=usertype, sel1=sel1, sel2=sel2, sel3=sel3, sel4=sel4)

if __name__ == "__main__":
    app.run()