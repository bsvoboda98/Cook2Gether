import {loremIpsum} from "lorem-ipsum";
function HomePage() {

    const loremText = loremIpsum({
        count: 1,
        units: 'paragraph',
        sentenceLowerBound: 5,
        sentenceUpperBound: 15
    });

    return(
        <>
            <h1>Wilkommen bei Cook2Gether!</h1>
            <p>{loremText}</p>
        </>
    )

}

export default HomePage;