// using http://werxltd.com/wp/2010/05/13/javascript-implementation-of-javas-string-hashcode-method/ implementation
String.prototype.hashCode = function() {
  var hash = 0, i, chr;
  if (this.length === 0) return hash;
  for (i = 0; i < this.length; i++) {
    chr   = this.charCodeAt(i);
    hash  = ((hash << 5) - hash) + chr;
    hash |= 0; // Convert to 32bit integer
  }
  return hash;
};
function all_good(){
  print("Great Job! Your validation code is:");
  print("LAB1 is all good".hashCode());
}

function check_version(members){
  return members.every(function(member, index, _arr){
    var mongo = Mongo(member['host']);
    return mongo.getDB('local').version().startsWith("4.0");
  });
}
var error = false;
var conf = rs.conf();

// check for replica set member size.
if(conf['members'].length != 3){
  print("The replica set cluster you have configured does not have the expected number of nodes!");
  error = true;
}

// check for node version
if(!check_version(conf['members'])){
  print("All members of the replica set need to be set using 4.0 version!");
  error = true;
}

if (!error){
  all_good()
}
