import abc

class WatroObject(object):
    _field_names = []
    def __init__(self, param_list):
        if (len(param_list) == len(self.field_names())):
            self._field_dict = {key: param_list[i] for i, key in enumerate(self.field_names())}
        else:
            raise ValueError("List of params not long enough")

    # key errors returned mean that variable doesn't exist
    def __getattribute__(self, attr):
        if attr in type(self).field_names():
            return self._field_dict.get(attr)
        else:
            return object.__getattribute__(self, attr)

    def __setattr__(self, attr, value):
        if attr != "_field_dict":
            self._field_dict[attr] = value
        else:
            object.__setattr__(self, attr, value)

    def firebase_object(self):
        return self._field_dict;

    def __str__(self):
        return "\n" + str(type(self)) + ": " + str([str(key) + ": " + str(value) + " " for key, value in self._field_dict.items()])


    @staticmethod
    @abc.abstractmethod
    def path():
        raise NotImplementedError()

    @classmethod
    def field_names(cls):
        return cls._field_names