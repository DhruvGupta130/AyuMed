import { useState } from 'react';
import DocumentUpload from './DocumentUpload.jsx';
import DocumentViewer from './DocumentViewer.jsx';

const PatientDocument = () => {
  const [upload, setUpload] = useState(false);

  return (
    <div className="patient-document">
      {upload ?(
        <DocumentUpload onClose={() => setUpload(false)} />
      ) :  (
        <DocumentViewer onUploadClick={() => setUpload(true)} />
      )}
    </div>
  );
};

export default PatientDocument;
