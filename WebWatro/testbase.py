from firebase import firebase as fbase
import model

class Database():
    #"https://watro-c0aaf.firebaseio.com/"
    def __init__(self, url):
        self.data_dict = fbase.FirebaseApplication(url, authentication = None)  
        # print(self.data_dict)
        # print(self.users.keys()[0])
    # self.data_dict = firebase.get('/Users', None)
    # for i, userID in enumerate(result):
    #     print(result.get(result.keys()[i]).get(result.get(result.keys()[i]).keys()[0]))

    #returns list of users by id
    @property
    def users(self):
        self._users = self.data_dict.get('/Users', None)
        return self._users

    def print_users(self):
        for i, userID in enumerate(self.users.keys()):
            print(str(self.users.get(userID)) + "\n")
            # print(self.users.get(userID).keys())
            # print(self.data_dict.get(self.data_dict.keys()[i]).get(self.data_dict.get(self.data_dict.keys()[i]).keys()[0]))

    def print_names(self):
        key = self.users.get(self.users.keys()[0]).keys()[0]
        for i, userID in enumerate(self.users.keys()):
            print(str(self.users.get(userID).get(key)) + "\n")

dbase = Database("https://watro-c0aaf.firebaseio.com/")
dbase.print_names()
