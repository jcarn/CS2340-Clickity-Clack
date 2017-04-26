import pyrebase
from model import user, report, purity_report
from model.user import User
from model.report import Report
from model.purity_report import PurityReport
class Database():
    def __init__(self):
        config = {
          "apiKey": "AIzaSyDHtZ8_XkQvC-c764cAOncFXIhPqO2p69I",
          "authDomain":  "watro-c0aaf.firebaseapp.com",
          "databaseURL": "https://watro-c0aaf.firebaseio.com/",
          "storageBucket": "gs://watro-c0aaf.appspot.com",
          "serviceAccount": "./Watro-eb70941f0251.json"
        }
        self.firebase = pyrebase.initialize_app(config)
        self.auth = self.firebase.auth()
        self._auth_token = None;
        self._current_user = None
        self.update()

    #returns list of users
    @property
    def users(self):
        self.update()
        return [User(param_list) for param_list in self._get_objects(User)]

    #returns list of reports
    @property
    def reports(self):
        self.update()
        return [report.Report(param_list) for param_list in self._get_objects(report.Report)]
    
    #returns list of purity_reports
    @property
    def purity_reports(self):
        self.update()
        return [purity_report.PurityReport(param_list) for param_list in self._get_objects(purity_report.PurityReport)]

    @property
    def current_user(self):
        return self.find_user(self._current_user.get("localId",self._auth_token))

    def find_user(self, id):
        for user in self.users:
            if user.id == id: return user

    def login(self, email, password):
        self._current_user = self.auth.sign_in_with_email_and_password(email, password)
        if self._current_user is not None:
            self._auth_token = self._current_user['idToken']

    # takes a user object and turns it into a firebase object, posts it
    def register_user(self, new_user, password):
        try:
            self.auth.create_user_with_email_and_password(new_user.email, password)
            self.login(new_user.email, password)
        except Exception as err:
            try: 
                
                self.login(new_user.email, password)
            except:
                print("Registration failed: " + str(err))
                return False
        new_user.id = self._current_user.get("localId")
        self.push_object(new_user)
        return True

    def update_object(self, updated_obj):
        self.push_object(updated_obj)

    def update(self):
        self.firedata = self.firebase.database()

    def push_object(self, obj):
        self.firedata.child(obj.path()).child(obj.id).set(obj.firebase_object(), self._auth_token)


    def _get_objects(self, object_type):
        return_list = []
        for obj in self.firedata.child(object_type.path()).get(self._auth_token).each():
            return_list.append([obj.val().get(key) for key in object_type.field_names()])
        return return_list