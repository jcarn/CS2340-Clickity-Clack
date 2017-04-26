from flask import *
from flask_googlemaps import GoogleMaps
from flask_googlemaps import Map
import firebase_handler
from model import user

user = ""

db = firebase_handler.Database()
app = Flask(__name__)

app.config['GOOGLEMAPS_KEY'] = "AIzaSyCPOAyJ7sYZgkBaeHKdkzJirKox8QLJs0U"
GoogleMaps(app)

@app.route("/")
def mainThing():
    # return "Hello World!"
    return render_template("home.html")

@app.route("/main")
# def mapHome():
#     return render_template("mapHome.html")
def main():
# creating a map in the view
    user = str(db.users[0].firstName)
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
    sndmap = Map(
    identifier="sndmap",
    lat=37.4419,
    lng=-122.1419,
    markers=[
    {
    'icon': '/static/images/minimarker.png',
    'lat': reports[0].latitude,
    'lng': reports[0].longitude,
    'infobox': "Hi i'm amy"
    },
    {
    'icon': '/static/images/minimarker.png',
    'lat': 37.4300,
    'lng': -122.1400,
    'infobox': "<b>Hello World from other place</b>"
    }
    ]
    )
    return render_template('mapHome.html', mymap=mymap, sndmap=sndmap, user=user)

#makes dictionary with marker attributes from a single report - to be added to marker list
def mapMarkers(report):
    locationDict = {}
    info = str(report.waterType) + ", " + str(report.waterCondition)
    locationDict['icon'] = '/static/images/minimarker.png'
    locationDict['lat'] = report.latitude
    locationDict['lng'] = report.longitude
    locationDict['infobox'] = info

    return locationDict


@app.route("/info")
def info():
    user = str(db.users[0])
    amy = "I AM AMY"
    print("Hi I am Amy " + user)
    return render_template('info.html', user=user, amy=amy)

@app.route("/login", methods = ['GET', 'POST'])
def login():
    global user

    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']
        db.login(username, password)

        user = db.current_user().firstName

        return redirect(url_for('main'))
    return render_template("login.html")

@app.route("/register")
def register():
    return render_template("register.html")

if __name__ == "__main__":
    app.run()