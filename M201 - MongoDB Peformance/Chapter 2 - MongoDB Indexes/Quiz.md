# Quiz

## Introduction to Indexes

**Problem:**

Which of the following statements regarding indexes are true?

- **Indexes are used to increase the speed of our queries.**
- **The _id field is automatically indexed on all collections.**
- **mIndexes reduce the number of documents MongoDB needs to examine to satisfy a query.**
- **Indexes can decrease write, update, and delete performance.**

## Single Field Indexes Part 2

**Problem:**

Which of the following queries can use an index on the zip field?

- **db.addresses.find( { zip : 55555 } )**
- db.addresses.find( { city : "Newark", state : "NJ" } )
- db.addresses.find()

## Sorting with Indexes

**Problem:**

Given the following schema for the products collection:

```
{
  "_id": ObjectId,
  "product_name": String,
  "product_id": String
}
```

And the following index on the products collection:

```
{ product_id: 1 }
```

Which of the following queries will use the given index to perform the sorting of the returned documents?

- **db.products.find({}).sort({ product_id: 1 })**
- **db.products.find({}).sort({ product_id: -1 })**
- **db.products.find({ product_name: 'Soap' }).sort({ product_id: 1 })**
- db.products.find({ product_name: 'Wax' }).sort({ product_name: 1 })
- **db.products.find({ product_id: '57d7a1' }).sort({ product_id: -1 })**

## Multikey Indexes

**Problem:**

Given the following index:

```
{ name: 1, emails: 1 }
```
When the following document is inserted, how many index entries will be created?

```
{
  "name": "Beatrice McBride",
  "age": 26,
  "emails": [
      "puovvid@wamaw.kp",
      "todujufo@zoehed.mh",
      "fakmir@cebfirvot.pm"
  ]
}
```

- 1
- 2
- **3**
- 4

## Partial Indexes

**Problem:**

Which of the following is true regarding partial indexes?

- **Partial indexes represent a superset of the functionality of sparse indexes.**
- **Partial indexes can be used to reduce the number of keys in an index.**
- Partial indexes don't support a uniqueness constraint.
- **Partial indexes support compound indexes.**

## Text Indexes

**Problem:**

Which other type of index is mostly closely related to text indexes?

- Single-key indexes
- Compound indexes
- **Multi-key indexes**
- Partial indexes

## Collations

**Problem:**

Which of the following statements are true regarding collations on indexes?

- MongoDB only allows collations to be defined at collection level
- **Collations allow the creation of case insensitive indexes**
- Creating an index with a different collation from the base collection implies overriding the base collection collation.
- **We can define specific collations in an index**
