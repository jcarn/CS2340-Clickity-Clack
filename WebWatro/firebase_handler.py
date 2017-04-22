import pyrebase
from model import user, report, purity_report

class Database():
    def __init__(self):
        config = {
          "apiKey": "AIzaSyDHtZ8_XkQvC-c764cAOncFXIhPqO2p69I",
          "authDomain":  "watro-c0aaf.firebaseapp.com",
          "databaseURL": "https://watro-c0aaf.firebaseio.com/",
          "storageBucket": "gs://watro-c0aaf.appspot.com"
        }
        self.firebase = pyrebase.initialize_app(config)
        self.auth = self.firebase.auth()
        self.update()
    #returns list of users
    @property
    def users(self):
        self.update()
        return [user.User(param_list) for param_list in self._get_objects(user.User)]

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
        return self.find_user(dbase._current_user.get("localId"))

    def find_user(self, ident):
        for user in self.users:
            if user.ident == ident: return user

    def login(self, email, password):
        self._current_user = self.auth.sign_in_with_email_and_password(email, password)

    def update(self):
        self.firedata = self.firebase.database()

    def _get_objects(self, object_type):
        return_list = []
        for obj in self.firedata.child(object_type.path()).get().each():
            return_list.append([obj.val().get(key) for key in object_type.param_list()])
        return return_list
