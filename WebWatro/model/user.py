from .watro_object import WatroObject
class User(WatroObject):
    _field_names = ["email", "id", "firstName", "lastName", "userType", "homeAddress"]
    @staticmethod
    def path():
        return "/Users"