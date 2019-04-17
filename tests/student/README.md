# Student Tests

Place your system tests in this directory, under subdirectories named by project.

#### 0_star.test

---

A file that has properly configured headers but no stars will still be read without any errors, but 0 stars will be read.

#### misformatted-star-headers.test

---

A file that has an extra comma in the header or stars files and properly formatted stars will throw an error.

A file that has an extra comma in one of the stars descriptions and a properly formatted header will throw an error. 

#### exact-radius-star.test

---

Radius search with radius 0 will only return a star that is exactly there, where a star exists at given coordinates.

#### exact-radius-mismatch-star.test

---

Radius search with radius 0 will only return a star that is exactly there, where a star does not exist at given coordinates.

#### fewer-neighbors-than-k.test

---

If there are not enough stars to find k-neighbors, the number of stars that were found will be returned. Also tests to make sure the named star will not be included. Also tries different combinations of single and double quotes.

#### radius-no-neighbors.test

---

Non-zero, positive radius search with no stars satisfying criteria radius search.

#### neighbors-tie.test

---

Tied neighbors, will return arbitrary star. Input hard-coded to match behavior of implementation but could vary in future implementations.