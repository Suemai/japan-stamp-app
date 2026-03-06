import { Image, Pressable } from 'react-native';

/* Todo:
- make button actually pressable, maybe with a bit on animation
- logic for adding stamps
*/

const ReportStamp = () => {

    const reportIcon = require("../assets/images/icons/validate-stamp.png");

    return (
        <Pressable
            onPress={() => {}}
            style={{
                alignItems: 'center',
                justifyContent: 'center',
                alignSelf: 'center',
                backgroundColor: '#ffffff',
                borderRadius: 12,
                width: '100%',
                height: '100%',
                shadowColor: '#000',
                shadowOffset: { width: 0, height: 2 },
                shadowOpacity: 0.18,
                shadowRadius: 6,
                elevation: 5,
            }}>
            <Image source={reportIcon}
                   resizeMode="contain"
                   tintColor="#000"
                   style={{
                       width: '60%',
                       height: '60%'}}/>
        </Pressable>
    );
}
export default ReportStamp;