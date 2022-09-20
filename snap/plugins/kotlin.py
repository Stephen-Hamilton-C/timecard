import snapcraft


class MyPlugin(snapcraft.BasePlugin):

    @classmethod
    def schema(cls):
        schema = super().schema()

        # Add a new property called "my-property"
        schema['properties']['my-property'] = {
            'type': 'string',
        }

        # The "my-option" property is now required
        # schema['required'].append('my-property')

        return schema

    def pull(self):
        super().pull()
        print('Pull done. Here is "my-property": {}'.format(
            self.options.my_property))

    def build(self):
        super().build()
        print('Build done.')
