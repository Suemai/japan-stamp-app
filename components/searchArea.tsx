import {Text, Pressable} from 'react-native';

/* Todo:
- make button actually pressable, maybe with a bit on animation
- logic for searching and fetching stamps in the area
*/

const SearchArea = () => {

    return (
        <Pressable
            onPress={() => {}}
            style={{
                flexDirection: 'row',
                alignItems: 'center',
                justifyContent: 'center',
                alignSelf: 'center',
                backgroundColor: '#ffffff',
                borderRadius: 50,
                paddingHorizontal: 20,
                paddingVertical: 10,
                shadowColor: '#000',
                shadowOffset: { width: 0, height: 2 },
                shadowOpacity: 0.18,
                shadowRadius: 6,
                elevation: 5,
                marginTop:5
            }}>
            <Text className={'text-primary-light font-bold font-size-14'}>
                Search this area
            </Text>
        </Pressable>
    );
}

export default SearchArea;