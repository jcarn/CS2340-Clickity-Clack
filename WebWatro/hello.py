from flask import *
from flask_googlemaps import GoogleMaps
from flask_googlemaps import Map
import firebase_handler
from model import user

# db = firebase_handler.Database("https://watro-c0aaf.firebaseio.com/")

app = Flask(__name__)

app.config['GOOGLEMAPS_KEY'] = "AIzaSyCPOAyJ7sYZgkBaeHKdkzJirKox8QLJs0U"
GoogleMaps(app)

@app.route("/")
def mainThing():
    # return "Hello World!"
    return render_template("home.html")

@app.route("/map")
# def mapHome():
#     return render_template("mapHome.html")
def mapview():
# creating a map in the view
    mymap = Map(
    identifier="view-side",
    lat=37.4419,
    lng=-122.1419,
    markers=[(37.4419, -122.1419)]
    )
    sndmap = Map(
    identifier="sndmap",
    lat=37.4419,
    lng=-122.1419,
    markers=[
    {
    'icon': 'http://maps.google.com/mapfiles/ms/icons/green-dot.png',
    'lat': 37.4419,
    'lng': -122.1419,
    'infobox': "<b>Hello World</b>"
    },
    {
    'icon': 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png',
    'lat': 37.4300,
    'lng': -122.1400,
    'infobox': "<b>Hello World from other place</b>"
    }
    ]
    )
    return render_template('mapHome.html', mymap=mymap, sndmap=sndmap)

@app.route("/info")
def info():
    user = str(db.users[0])
    amy = "I AM AMY"
    print("Hi I am Amy " + user)
    return render_template('info.html', user=user, amy=amy)


if __name__ == "__main__":
    app.run()