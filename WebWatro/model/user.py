import generic_user
class User(generic_user.GenericUser):

    def __init__(self, param_list):
        if (len(param_list) == 6):
            super(User, self).__init__(param_list[5], param_list[2])
            self.first_name = param_list[0]
            self.last_name = param_list[1]
            self._user_type = param_list[3]
            self.address = param_list[4]

    @property 
    def user_type(self):
        return self._user_type

    @user_type.setter
    def user_type(self, value):
        if (value in ["User", "Worker", "Manager", "Administrator"]):
            self._user_type = value

    def __str__(self):
        names = "" + str(self.first_name) + str(self.last_name) + str(self.address) + str(self.ident) + str(self.email)
        return names