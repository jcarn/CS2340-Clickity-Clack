from firebase import firebase as fbase
from model import user, report, purity_report
class Database():
    #"https://watro-c0aaf.firebaseio.com/"
    def __init__(self, url):
        self.firedata = fbase.FirebaseApplication(url, authentication = None)  
        # print(self.firedata)
        # print(self.users.keys()[0])
    # self.firedata = firebase.get('/Users', None)
    # for i, userID in enumerate(result):
    #     print(result.get(result.keys()[i]).get(result.get(result.keys()[i]).keys()[0]))

    #returns list of users
    @property
    def users(self):
        return [user.User(param_list) for param_list in self._get_objects('/Users')]

    #returns list of reports
    @property
    def reports(self):
        return [report.Report(param_list) for param_list in self._get_objects('/Reports')]

    @property
    def purity_reports(self):
        return [purity_report.PurityReport(param_list) for param_list in self._get_objects('/PurityReports')]

    def _get_objects(self, object_type):
        object_dict = self.firedata.get(object_type, None)
        object_list = []
        #get key set of possible fields for object
        key_set = object_dict.get(object_dict.keys()[0]).keys()
        for i, objectID in enumerate(object_dict):
            # for cur_field in key_set:
            #     print(object_dict.get(objectID).get(cur_field))
            object_list.append([(object_dict.get(objectID).get(key)) for key in key_set])
        return object_list



dbase = Database("https://watro-c0aaf.firebaseio.com/")
x = dbase.users
y = dbase.reports
z = dbase.purity_reports
print z[0]
