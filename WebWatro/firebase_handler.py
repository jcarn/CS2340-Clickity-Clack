import pyrebase

from model import user, report, purity_report
class Database():
    #"https://watro-c0aaf.firebaseio.com/"
    def __init__(self, url):
        config = {
          "apiKey": "AIzaSyDHtZ8_XkQvC-c764cAOncFXIhPqO2p69I",
          "authDomain":  "watro-c0aaf.firebaseapp.com",
          "databaseURL": "https://watro-c0aaf.firebaseio.com/",
          "storageBucket": "gs://watro-c0aaf.appspot.com"
        }
        firebase = pyrebase.initialize_app(config)
        self.firedata = firebase.database()

    #returns list of users
    @property
    def users(self):
        return [user.User(param_list) for param_list in self._get_objects(user.User)]

    #returns list of reports
    @property
    def reports(self):
        return [report.Report(param_list) for param_list in self._get_objects(report.Report)]
    
    #returns list of purity_reports
    @property
    def purity_reports(self):
        return [purity_report.PurityReport(param_list) for param_list in self._get_objects(purity_report.PurityReport)]

    def _get_objects(self, object_type):
        return_list = []
        for obj in self.firedata.child(object_type.path()).get().each():
            return_list.append([obj.val().get(key) for key in object_type.param_list()])
        return return_list

dbase = Database("https://watro-c0aaf.firebaseio.com/")
for pr in dbase.purity_reports:
    print(pr)
